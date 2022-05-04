export function initNodeParams(node){
    if(!node.params){
        node.params = {}
    }
    if(node.type === 'read_txt'){
        if(node.params.file){
            node.fileList = [{
                name: node.params.file.name
            }]
        }else{
            node.fileList = []
            node.params = {
                file: null,
                outputParamName: null
            }
        }
    }else if(node.type === 'read_excel'){
        if(node.params.file){
            node.fileList = [{
                name: node.params.file.name
            }]
        }else{
            node.fileList = []
            node.params = {
                file: null,
                outputParamName: null,
                sheetName: null,
                colNamePos: 0
            }
        }
    }
    return node;
}
