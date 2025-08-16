// modalMessage
import { showModal, closeModal } from '../../screen/modal';
import { IElementEvent } from '../elementEvent';
import { Modal } from 'bootstrap';

export default class UpdateMessageEvent implements IElementEvent {

  execute(): void {
    showModal('updateMessage', 'Updating...', []);
  }

  close() {
    closeModal();
  }
}
