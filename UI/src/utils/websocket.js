
const BASE_URL = process.env.VUE_APP_BASE_BACKEND_URL

function getWebsocketURL(){
  if(BASE_URL.indexOf('https') >= 0){
    return BASE_URL.replace('https', 'ws');
  }else if(BASE_URL.indexOf('http') >= 0){
    return BASE_URL.replace('http', 'ws');
  }
  return 'ws://localhost:8080'
}

export function websocketInit(info){
  return getWebsocketURL() + '/rpa/task/websocket/' + info;
}
