export function initNodeParams(node){
    if(!node.params){
        node.params = {}
    }
    if(node.type === 'read_txt'){
        if(node.params.file){
            node.fileList = [{
                name: node.params.fileName
            }]
        }else{
            node.fileList = []
        }
    }else if(node.type === 'read_excel'){
        if(node.params.file){
            node.fileList = [{
                name: node.params.fileName
            }]
        }else{
            node.fileList = []
        }
    }
    return node;
}

export function blobToBase64(blob) {
    return new Promise((resolve, reject) => {
        const fileReader = new FileReader();
        fileReader.onload = (e) => {
            resolve(e.target.result);
        };
        // readAsDataURL
        fileReader.readAsDataURL(blob);
        fileReader.onerror = () => {
            reject(new Error('blobToBase64 error'));
        };
    });
}
