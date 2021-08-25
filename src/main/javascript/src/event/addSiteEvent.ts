import { IElementEvent } from './elementEvent';
import { Modal } from 'bootstrap';

export default class AddSiteEvent implements IElementEvent {
  execute() {
    console.log('AddSiteEvent');

    var modal = new Modal(document.getElementById('exampleModal'));
    modal.show();
  }
}
