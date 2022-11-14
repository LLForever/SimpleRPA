from wsgiref.simple_server import make_server
import json
import paddlehub as hub
import cv2
import base64
from io import BytesIO
import numpy as np
from PIL import Image
import uuid
import os
from table_ana_tool import *
from paddlenlp import Taskflow

errStr = {
    "code" : -1,
    "msg" : "not support"
}
errStr = str(errStr)

def getSuccessStr(res):
    #拼装回复报文
    successStr = {}
    successStr["res"] = res
    return successStr

def getOcrOriginRes(current_req_json):
    current_data = current_req_json.get("imageByte")
    current_data = base64.b64decode(current_data)
    image_data = BytesIO(current_data)
    img = Image.open(image_data)
    get_timestamp_uuid = uuid.uuid1()
    imgName = str(get_timestamp_uuid) + ".png"
    img.save(imgName)

    ocr = hub.Module(name="chinese_ocr_db_crnn_mobile", enable_mkldnn=True)       # mkldnn加速仅在CPU下有效
    successStr = ocr.recognize_text(images=[cv2.imread(imgName)])
    successStr = getSuccessStr(successStr[0].get("data"))

    os.remove(imgName)
    return successStr

def getOcrRes(current_req_json):
    successStr = getOcrOriginRes(current_req_json)
    return str(successStr)

def getTableOcrRes(current_req_json):
    current_data = current_req_json.get("imageByte")
    current_data = base64.b64decode(current_data)
    image_data = BytesIO(current_data)
    img = Image.open(image_data)
    get_timestamp_uuid = uuid.uuid1()
    imgName = str(get_timestamp_uuid) + ".png"
    img.save(imgName)

    table_sys = TableSystem()

    img = cv2.imread(imgName)
    pred_res, _ = table_sys(img)

    os.remove(imgName)

    successStr = getSuccessStr(pred_res['html'])

    return str(successStr)

def getWordExtraRes(current_req_json):
    strRes = getOcrOriginRes(current_req_json)
    dictList = strRes['res']
    schema = current_req_json.get("schema")
    strRes = ''
    for i in range(0, len(dictList)):
        strRes += str(dictList[i]['text'])
        strRes += ","
    ie = Taskflow('information_extraction', schema=schema)
    return str(getSuccessStr(ie(strRes)))

def AIRunFactory(current_req_json):
    data_type = current_req_json.get("type")
    if data_type == "OCR":
        return getOcrRes(current_req_json)
    if data_type == "TABLE_OCR":
        return getTableOcrRes(current_req_json)
    if data_type == "KEY_WORD_EXTRA":
        return getWordExtraRes(current_req_json)
    return str(getSuccessStr("None Type Detect"))

def RunServer(environ, start_response):

    #添加回复内容的HTTP头部信息，支持多个
    headers = {'Content-Type': 'application/json', 'Custom-head1': 'Custom-info1'}

    # environ 包含当前环境信息与请求信息，为字符串类型的键值对
    current_url = environ['PATH_INFO']
    current_content_type = environ['CONTENT_TYPE']
    current_content_length = environ['CONTENT_LENGTH']
    current_request_method = environ['REQUEST_METHOD']
    current_remote_address = environ['REMOTE_ADDR']
    #current_encode_type = environ['PYTHONIOENCODING']        #获取当前文字编码格式，默认为UTF-8

    #根据不同URL回复不同内容
    if current_url == "/aienhance":
        if(current_request_method == 'POST'):
            current_req_body = environ['wsgi.input'].read(int(environ['CONTENT_LENGTH']))
            current_req_json = json.loads(current_req_body)
            successStr = AIRunFactory(current_req_json)
        else:
            # successStr = getOcrRes([cv2.imread('/rpadata/test/test.png')])
            successStr = {}
            successStr["res"] = "none operation"
            successStr = str(successStr)
        start_response("200 OK", list(headers.items()))
        return [successStr.encode("utf-8"), ]

    else:
        start_response("404 not found", list(headers.items()))
        return [errStr.encode("utf-8"), ]

if __name__ == "__main__":
    #10000为HTTP服务监听端口，自行修改
    httpd = make_server('', 10000, RunServer)
    host, port = httpd.socket.getsockname()
    print('Serving running', host, 'port', port)
    httpd.serve_forever()

