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
from paddlenlp import Taskflow
from pyMultiobjective.algorithm import non_dominated_sorting_genetic_algorithm_III

class FitnessFuncLib(object) :
    @staticmethod
    def  getFitFromFunc(var, type) :
        if (type == 1) :
            return FitnessFuncLib.Func1(var)
        elif(type == 2) :
            return FitnessFuncLib.Func2(var)
        elif(type == 3) :
            return FitnessFuncLib.Func3(var)
        elif(type == 4) :
            return FitnessFuncLib.Func4(var)
        elif(type == 5) :
            return FitnessFuncLib.Func5(var)
        elif(type == 6) :
            return FitnessFuncLib.Func6(var)
        elif(type == 7) :
            return FitnessFuncLib.Func7(var)
        elif(type == 8) :
            return FitnessFuncLib.Func8(var)
        elif(type == 9) :
            return FitnessFuncLib.Func9(var)
        elif(type == 10) :
            return FitnessFuncLib.Func10(var)
        elif(type == 11) :
            return FitnessFuncLib.Func11(var)
        elif(type == 12) :
            return FitnessFuncLib.Func12(var)
        elif(type == 13) :
            return FitnessFuncLib.Func13(var)
        return 1.7976931348623157E308
    @staticmethod
    def  getLow( type) :
        if (type == 1) :
            return FitnessFuncLib.getFunc1Low()
        elif(type == 2) :
            return FitnessFuncLib.getFunc2Low()
        elif(type == 3) :
            return FitnessFuncLib.getFunc3Low()
        elif(type == 4) :
            return FitnessFuncLib.getFunc4Low()
        elif(type == 5) :
            return FitnessFuncLib.getFunc5Low()
        elif(type == 6) :
            return FitnessFuncLib.getFunc6Low()
        elif(type == 7) :
            return FitnessFuncLib.getFunc7Low()
        elif(type == 8) :
            return FitnessFuncLib.getFunc8Low()
        elif(type == 9) :
            return FitnessFuncLib.getFunc9Low()
        elif(type == 10) :
            return FitnessFuncLib.getFunc10Low()
        elif(type == 11) :
            return FitnessFuncLib.getFunc11Low()
        elif(type == 12) :
            return FitnessFuncLib.getFunc12Low()
        elif(type == 13) :
            return FitnessFuncLib.getFunc13Low()
        return None
    @staticmethod
    def  getUp( type) :
        if (type == 1) :
            return FitnessFuncLib.getFunc1Up()
        elif(type == 2) :
            return FitnessFuncLib.getFunc2up()
        elif(type == 3) :
            return FitnessFuncLib.getFunc3Up()
        elif(type == 4) :
            return FitnessFuncLib.getFunc4Up()
        elif(type == 5) :
            return FitnessFuncLib.getFunc5Up()
        elif(type == 6) :
            return FitnessFuncLib.getFunc6Up()
        elif(type == 7) :
            return FitnessFuncLib.getFunc7Up()
        elif(type == 8) :
            return FitnessFuncLib.getFunc8Up()
        elif(type == 9) :
            return FitnessFuncLib.getFunc9Up()
        elif(type == 10) :
            return FitnessFuncLib.getFunc10Up()
        elif(type == 11) :
            return FitnessFuncLib.getFunc11Up()
        elif(type == 12) :
            return FitnessFuncLib.getFunc12Up()
        elif(type == 13) :
            return FitnessFuncLib.getFunc13Up()
        return None
    @staticmethod
    def  Func1( var) :
        sum = 0
        i = 0
        while (i < len(var)) :
            sum += var[i] * var[i]
            i += 1
        return sum
    @staticmethod
    def  getFunc1Low() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = -100
            j += 1
        return d
    @staticmethod
    def  getFunc1Up() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = 100
            j += 1
        return d
    @staticmethod
    def  Func2( var) :
        sum = 0
        i = 0
        while (i < len(var)) :
            tmp = 0
            j = 0
            while (j <= i) :
                tmp += var[j]
                j += 1
            sum += tmp * tmp
            i += 1
        return sum
    @staticmethod
    def  getFunc2Low() :
        return FitnessFuncLib.getFunc1Low()
    @staticmethod
    def  getFunc2up() :
        return FitnessFuncLib.getFunc1Up()
    @staticmethod
    def  Func3( var) :
        sum = 0
        i = 0
        while (i < len(var)) :
            sum += (var[i] + 0.5) * (var[i] + 0.5)
            i += 1
        return sum
    @staticmethod
    def  getFunc3Low() :
        return FitnessFuncLib.getFunc1Low()
    @staticmethod
    def  getFunc3Up() :
        return FitnessFuncLib.getFunc1Up()
    @staticmethod
    def  Func4( var) :
        y = 0.0
        if (len(var) < 2) :
            y = 9.42
        else :
            y = var[1]
        return math.pow((y - (5.1 / (4 * math.pi * math.pi)) * var[0] * var[0] + (5 / math.pi) * var[0] - 6),2) + 10 + 10 * (1 - (1 / (8 * math.pi)) * math.cos(var[0]))
    @staticmethod
    def  getFunc4Low() :
        d = [0.0] * (2)
        d[0] = -5
        d[1] = 0
        return d
    @staticmethod
    def  getFunc4Up() :
        d = [0.0] * (2)
        d[0] = 10
        d[1] = 15
        return d
    @staticmethod
    def  Func5( var) :
        sum = 0
        i = 0
        while (i < len(var) - 1) :
            sum += 100 * (var[i + 1] - var[i] * var[i]) * (var[i + 1] - var[i] * var[i]) + (var[i] - 1) * (var[i] - 1)
            i += 1
        return sum
    @staticmethod
    def  getFunc5Low() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = -5
            j += 1
        return d
    @staticmethod
    def  getFunc5Up() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = 10
            j += 1
        return d
    @staticmethod
    def  Func6( var) :
        y = 0.0
        sum = 0
        if (len(var) < 2) :
            y = -0.5471
        else :
            y = var[1]
        sum += 1 + 2.5 * y - 1.5 * var[0]
        sum += math.sin(var[0] + y)
        sum += (y - var[0]) * (y - var[0])
        return sum
    @staticmethod
    def  getFunc6Low() :
        d = [0.0] * (2)
        d[0] = -1.5
        d[1] = -3
        return d
    @staticmethod
    def  getFunc6Up() :
        d = [0.0] * (2)
        d[0] = 4
        d[1] = 4
        return d
    @staticmethod
    def  Func7( var) :
        y = 0.0
        sum = 0
        if (len(var) < 2) :
            y = 0.5
        else :
            y = var[1]
        sum += math.pow((1.5 - var[0] + var[0] * y),2)
        sum += math.pow((2.25 - var[0] + var[0] * y * y),2)
        sum += math.pow((2.625 - var[0] + var[0] * y * y * y),2)
        return sum
    @staticmethod
    def  getFunc7Low() :
        dimension = 2
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = -4.5
            j += 1
        return d
    @staticmethod
    def  getFunc7Up() :
        dimension = 2
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = 4.5
            j += 1
        return d
    @staticmethod
    def  Func8( var) :
        y = 0.0
        sum = 0
        if (len(var) < 2) :
            y = 1
        else :
            y = var[1]
        sum += 100 * mathsqrt(abs(y - 0.01 * var[0] * var[0]))
        sum += 0.01 * abs(var[0] + 10)
        return sum
    @staticmethod
    def  getFunc8Low() :
        d = [0.0] * (2)
        d[0] = -15
        d[1] = -3
        return d
    @staticmethod
    def  getFunc8Up() :
        d = [0.0] * (2)
        d[0] = -5
        d[1] = 3
        return d
    @staticmethod
    def  Func9( var) :
        sum = 0
        i = 0
        while (i < len(var)) :
            sum += -var[i] * math.sin(mathsqrt(abs(var[i])))
            i += 1
        return sum
    @staticmethod
    def  getFunc9Low() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = -500
            j += 1
        return d
    @staticmethod
    def  getFunc9Up() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = 500
            j += 1
        return d
    @staticmethod
    def  Func10( var) :
        sum = 0
        i = 0
        while (i < len(var)) :
            sum += 10 + var[i] * var[i] - 10 * math.cos(2 * math.pi * var[i])
            i += 1
        return sum
    @staticmethod
    def  getFunc10Low() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = -5.12
            j += 1
        return d
    @staticmethod
    def  getFunc10Up() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = 5.12
            j += 1
        return d
    @staticmethod
    def  Func11( var) :
        sum = 0
        i = 0
        while (i < len(var)) :
            y = var[i]
            if (abs(var[i]) < 0.5) :
                y = var[i]
            else :
                y = math.round(2 * y) / 2.0
            sum += y * y - 10 * math.cos(2 * math.pi * y) + 10
            i += 1
        return sum
    @staticmethod
    def  getFunc11Low() :
        return FitnessFuncLib.getFunc10Low()
    @staticmethod
    def  getFunc11Up() :
        return FitnessFuncLib.getFunc10Up()
    @staticmethod
    def  Func12( var) :
        sum = 0
        sum += -20 * math.exp(-0.2 * mathsqrt((1.0 / len(var)) * FitnessFuncLib.Func1(var)))
        tmp = 0
        i = 0
        while (i < len(var)) :
            tmp += math.cos(2 * math.pi * var[i])
            i += 1
        sum += -math.exp((1.0 / len(var)) * tmp + 20)
        return sum
    @staticmethod
    def  getFunc12Low() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = -32
            j += 1
        return d
    @staticmethod
    def  getFunc12Up() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = 32
            j += 1
        return d
    @staticmethod
    def  Func13( var) :
        sum = 0
        sum += FitnessFuncLib.Func1(var) / 4000
        sum += 1
        multi = 1
        i = 0
        while (i < len(var)) :
            multi *= math.cos(var[i] / mathsqrt(i + 1.0))
            i += 1
        sum += -multi
        return sum

    @staticmethod
    def  getFunc13Low() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = -600
            j += 1
        return d

    @staticmethod
    def  getFunc13Up() :
        dimension = 30
        d = [0.0] * (dimension)
        j = 0
        while (j < dimension) :
            d[j] = 600
            j += 1
        return d


class NSGA_III:
    def __init__(self, type):
        self.type = type

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
        return a

    def run_test(self):
        min_values = FitnessFuncLib.getLow(self.type)
        max_values = FitnessFuncLib.getUp(self.type)

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
        sol = non_dominated_sorting_genetic_algorithm_III(list_of_functions = [self.FuncTest, self.FuncZero], **parameters)
        return sol

    def FuncTest(self, variables=[0,0]):
        return FitnessFuncLib.getFitFromFunc(variables, self.type)

    def FuncZero(self, variables=[0,0]):
        return 0

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
        T = [0, 0, 0, 0]
        for i in range(self.n):
            target = abs(int(variables[i]))
            if target > 3:
                target = 3
            costList = self.taskCostMap[self.ids[i]]
            sumList[target][0] += costList[0]
            sumList[target][1] += costList[1]
            sumList[target][2] += costList[2]
            T[target] += costList[3]
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
            #sumList[i][0] = self.checkValueAndChange(sumList[i][0])
            #sumList[i][1] = self.checkValueAndChange(sumList[i][1])
            #sumList[i][2] = self.checkValueAndChange(sumList[i][2])
            F += math.sqrt((sumList[i][0]/Rc)*(sumList[i][0]/Rc) + (sumList[i][1]/Rm)*(sumList[i][1]/Rm) + (sumList[i][2]/Rn)*(sumList[i][2]/Rn))*T[target]
        return 0.2*F


    def funG(self, variables=[0,0]):
        sumList = [[0,0,0],[0,0,0],[0,0,0],[0,0,0]]
        maxBalance = -1
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
            maxBalance = max(maxBalance, math.sqrt((sumList[i][0] + Nc)*(sumList[i][0] + Nc) + (sumList[i][1] + Nm)*(sumList[i][1] + Nm) + (sumList[i][2] + Nn)*(sumList[i][2] + Nn)) )
            F += math.sqrt((sumList[i][0] + Nc)*(sumList[i][0] + Nc) + (sumList[i][1] + Nm)*(sumList[i][1] + Nm) + (sumList[i][2] + Nn)*(sumList[i][2] + Nn))
        return 0.5*0.8*(F + 4*maxBalance)

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

def AIRunFactory(current_req_json):
    data_type = current_req_json.get("type")
    if data_type == "OCR":
        return getOcrRes(current_req_json)
    if data_type == "TABLE_OCR":
        return getTableOcrRes(current_req_json)
    if data_type == 'NSGA3':
        ids = current_req_json.get('ids')
        mai = current_req_json.get('mai')
        mpj = current_req_json.get('mpj')
        nsga3 = NSGA_III(ids, mai, mpj)
        sol = nsga3.run()
        return str(getSuccessStr(" ".join('%f'  %id for id in sol[0])))
    if data_type == 'NSGA3-TEST':
        type = current_req_json.get('type_int')
        nsga3 = NSGA_III(type)
        sol = nsga3.run_test()
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