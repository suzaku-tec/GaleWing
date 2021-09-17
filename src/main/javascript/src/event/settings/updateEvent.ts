import { IElementEvent } from '../elementEvent';

export default class UpdateEvent implements IElementEvent {
  execute(): void {
    var tags = document.getElementsByTagName('form');
    tags[0].method = 'POST';
    tags[0].action = '/settings/update';
    tags[0].submit();
  }
}
