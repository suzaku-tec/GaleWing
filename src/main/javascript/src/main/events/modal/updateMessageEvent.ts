// modalMessage
import { IElementEvent } from '../elementEvent';
import { Modal } from 'bootstrap';

export default class UpdateMessageEvent implements IElementEvent {
  private modal: Modal;

  execute(): void {
    var modalBody = document.getElementById('modal-body');

    var modalAddSiteBody = document.getElementById('updateMessageModal');

    modalBody.appendChild(modalAddSiteBody.cloneNode(true));

    var modalTitle = document.getElementById('exampleModalLabel');
    modalTitle.innerText = 'Updating...';

    var modalHeaderCloseBtn = document.getElementById('modal-header-btn-close');
    modalHeaderCloseBtn.style.display = 'none';

    document.getElementById('exampleModal').addEventListener(
      'hidden.bs.modal',
      (event) => {
        while (modalBody.firstChild) {
          modalBody.removeChild(modalBody.firstChild);
        }

        modalHeaderCloseBtn.style.display = '';

        document.getElementById('modal-dialog').classList.remove('modal-dialog-centered ');

        this.modal.dispose();
      },
      { once: true },
    );

    var a = document.getElementById('modal-dialog');
    a.classList.add('modal-dialog-centered');

    this.modal = new Modal(document.getElementById('exampleModal'), {
      backdrop: 'static',
    });

    this.modal.show();
  }

  close() {
    if (this.modal) {
      this.modal.hide();
    }
  }
}
