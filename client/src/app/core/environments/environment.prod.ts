import { RxStompConfig } from '@stomp/rx-stomp';

export const environment = {
  applicationName: 'Chat',
  apiUrl: 'https://chat-grsu-server.onrender.com/',
  production: true,
  stompConfig: {
    brokerURL: 'ws://chat-grsu-server.onrender.com/ws',
    connectionTimeout: 10000,
    reconnectDelay: 5000,
  } as RxStompConfig,
};
