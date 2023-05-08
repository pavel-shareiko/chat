import { RxStompConfig } from '@stomp/rx-stomp';

export const environment = {
  production: false,
  stompConfig: {
    brokerURL: 'ws://localhost:8080/ws',
    connectionTimeout: 10000,
    reconnectDelay: 5000,
    // debug: msg => {
    //   console.log(msg);
    // },
  } as RxStompConfig,
};
