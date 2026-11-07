// modalMessage
import { showModal, closeModal } from '../../screen/modal';
import { IElementEvent } from '../elementEvent';

export default class UpdateMessageEvent implements IElementEvent {

  execute(): void {
    showModal('updateMessage', 'Updating...', []);
  }

  close() {
    closeModal();
  }
}
