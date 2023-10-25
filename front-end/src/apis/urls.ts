const DEV = process.env.NODE_ENV !== 'production';

const PORT_WEB = ':3000';
const PORT_SERVER = ':8081';
const PORT_OPENVIDU = ':4443';

const URL_LOCAL = 'http://localhost';
const URL_RELEASE = 'https://k9c103.p.ssafy.io';

// export const WS_BASE_URL =
//   (DEV ? `ws://localhost${PORT_SERVER}` : 'wss://k9c103.p.ssafy.io') + '/ws';
export const WS_BASE_URL = 'wss://k9c103.p.ssafy.io' + '/ws';

// export const API_BASE_URL = (DEV ? URL_RELEASE : URL_RELEASE) + '/api/v1';
export const API_BASE_URL = URL_RELEASE + '/api/v1';
