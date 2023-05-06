import { environment } from 'src/app/environment/environment';
import { RxStompService } from './rx-stomp.service';

export function rxStompServiceFactory() {
  const rxStomp = new RxStompService();
  rxStomp.configure({
    ...environment.stompConfig,
    connectHeaders: {
      Authorization: `Bearer ${localStorage.getItem('id_token')}`,
    },
  });
  rxStomp.activate();
  return rxStomp;
}
