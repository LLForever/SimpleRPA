from wsgiref.simple_server import make_server
import json
import math
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
from pyMultiobjective.algorithm import non_dominated_sorting_genetic_algorithm_III

class NSGA_III:
    def __init__(self, ids, machinesAllocateInfo, machinePerformanceJSON):
        self.ids = ids
        self.n = len(ids)
        self.machinesAllocateInfo = machinesAllocateInfo
        self.machinePerformanceJSON = machinePerformanceJSON
        self.machineName = ['master', 'node1', 'node2', 'node3']
        self.taskCostMap={32:[19.875, 215.73, 6.498, 74.0], 34:[8.92, 117.91, 6.988, 27.0], 35:[7.645, 190.16, 1.915, 87.0], 36:[5.3149999999999995, 124.59, 1.382, 32.0], 38:[9.13, 115.48, 1.074, 189.0], 16:[10.089, 166.39, 13.751, 31.0], 17:[8.253, 219.39, 31.686, 15.0], 18:[7.5335, 156.14, 8.698, 60.0], 19:[8.413499999999999, 164.98, 14.308, 32.0], 20:[8.3995, 151.14, 9.953, 46.0], 21:[18.915, 837.43, 10.192, 57.0], 22:[20.755, 1025.56, 9.518, 63.0], 23:[7.005, 113.37, 34.891, 14.0], 24:[2.56, 1.0, 0.0, 1.0], 25:[8.51, 142.07, 10.172, 44.0], 26:[31.375, 1701.99, 3.949, 149.0], 27:[8.98, 121.19, 14.857, 28.0], 28:[9.035, 124.32, 13.765, 29.0], 29:[8.844999999999999, 117.75, 33.72, 14.0], 30:[11.17, 172.18, 16.325, 30.0], 31:[12.795000000000002, 252.16, 11.53, 58.0]}

    def getMemCost(self, machine, mem):
        if machine == 0:
            return 100*mem / 9787.14
        elif machine == 3:
            return 100*mem / 7535.09
        return 100*mem / 7819.15

    def checkValueAndChange(self, a):
        a = math.exp(a/10)
        return a

    def run(self):
        min_values = []
        max_values = []
        for i in range(self.n):
            min_values.append(-4)
            max_values.append(4)

        parameters = {
            'references': 5,
            'min_values': min_values,
            'max_values': max_values,
            'mutation_rate': 0.1,
            'generations': 500,
            'mu': 1,
            'eta': 1,
            'k': 2, 
            'verbose': False
        }
        sol = non_dominated_sorting_genetic_algorithm_III(list_of_functions = [self.funF, self.funG], **parameters)
        return sol

    def funF(self, variables=[0,0]):
        sumList = [[0,0,0],[0,0,0],[0,0,0],[0,0,0]]
        for i in range(self.n):
            target = abs(int(variables[i]))
            if target > 3:
                target = 3
            costList = self.taskCostMap[self.ids[i]]
            sumList[target][0] += costList[0]
            sumList[target][1] += costList[1]
            sumList[target][2] += costList[2]
        for i in range(4):
            machineName = self.machineName[i]
            machineCostList = self.machinesAllocateInfo.get(machineName).get('id_list')
            sumList[i][0] += machineCostList[0]
            sumList[i][1] += machineCostList[1]
            sumList[i][2] += machineCostList[2]
        F = 0
        for i in range(4):
            sumList[i][1] = self.getMemCost(i, sumList[i][1])
            Nc = self.machinePerformanceJSON.get(self.machineName[i]).get('cpu')
            Nm = self.machinePerformanceJSON.get(self.machineName[i]).get('mem')
            Nn = self.machinePerformanceJSON.get(self.machineName[i]).get('net')
            Rc = 100-Nc
            Rm = 100-Nm
            Rn = 100-Nn
            sumList[i][0] = self.checkValueAndChange(sumList[i][0])
            sumList[i][1] = self.checkValueAndChange(sumList[i][1])
            sumList[i][2] = self.checkValueAndChange(sumList[i][2])
            F += math.sqrt((sumList[i][0]/Rc)*(sumList[i][0]/Rc) + (sumList[i][1]/Rm)*(sumList[i][1]/Rm) + (sumList[i][2]/Rn)*(sumList[i][2]/Rn))
        return 0.5*F


    def funG(self, variables=[0,0]):
        sumList = [[0,0,0],[0,0,0],[0,0,0],[0,0,0]]
        for i in range(self.n):
            target = abs(int(variables[i]))
            if target > 3:
                target = 3
            costList = self.taskCostMap[self.ids[i]]
            sumList[target][0] += costList[0]
            sumList[target][1] += costList[1]
            sumList[target][2] += costList[2]
        for i in range(4):
            machineName = self.machineName[i]
            machineCostList = self.machinesAllocateInfo.get(machineName).get('id_list')
            sumList[i][0] += machineCostList[0]
            sumList[i][1] += machineCostList[1]
            sumList[i][2] += machineCostList[2]
        F = 0
        for i in range(4):
            sumList[i][1] = self.getMemCost(i, sumList[i][1])
            Nc = self.machinePerformanceJSON.get(self.machineName[i]).get('cpu')
            Nm = self.machinePerformanceJSON.get(self.machineName[i]).get('mem')
            Nn = self.machinePerformanceJSON.get(self.machineName[i]).get('net')
            Rc = 100-Nc
            Rm = 100-Nm
            Rn = 100-Nn
            sumList[i][0] = self.checkValueAndChange(sumList[i][0])
            sumList[i][1] = self.checkValueAndChange(sumList[i][1])
            sumList[i][2] = self.checkValueAndChange(sumList[i][2])
            F += math.sqrt((sumList[i][0] + Nc)*(sumList[i][0] + Nc) + (sumList[i][1] + Nm)*(sumList[i][1] + Nm) + (sumList[i][2] + Nn)*(sumList[i][2] + Nn))
        return 0.5*F

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
    if data_type == 'NSGA3':
        ids = current_req_json.get('ids');
        mai = current_req_json.get('mai');
        mpj = current_req_json.get('mpj');
        nsga3 = NSGA_III(ids, mai, mpj)
        sol = nsga3.run()
        return str(getSuccessStr(" ".join('%f'  %id for id in sol[0])))
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

