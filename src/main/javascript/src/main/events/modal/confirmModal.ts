import { Modal } from 'bootstrap';
import { IElementEvent } from '../elementEvent';

export default class ConfirmModalEvent {
  private modal: Modal;

  constructor(msg: string, execute: () => void) {
    var div = document.createElement('div');
    div.innerText = msg;

    var modalBody = document.getElementById('modal-body');
    modalBody.appendChild(div);

    var footer = document.getElementById('modal-footer');
    var confirmFooter = document.getElementsByClassName('confirmFooter')[0];
    footer.appendChild(confirmFooter.cloneNode(true));
    footer
      .getElementsByClassName('confirmOk')
      .item(0)
      .addEventListener('click', () => {
        execute();

        if (this.modal) {
          this.modal.hide();
        }
      });

    var modalHeaderCloseBtn = document.getElementById('modal-header-btn-close');
    modalHeaderCloseBtn.style.display = 'none';

    document.getElementById('exampleModal').addEventListener(
      'hidden.bs.modal',
      (event) => {
        // body削除
        while (modalBody.firstChild) {
          modalBody.removeChild(modalBody.firstChild);
        }

        // footer削除
        while (footer.firstChild) {
          footer.removeChild(footer.firstChild);
        }

        modalHeaderCloseBtn.style.display = '';

        this.modal.dispose();
      },
      { once: true },
    );

    this.modal = new Modal(document.getElementById('exampleModal'), {
      backdrop: 'static',
    });
    this.modal.show();
  }

  close() {
    if (this.modal) {
      this.modal.hide();
      document.getElementById('modal-dialog').classList.remove('modal-dialog-centered');
    }
  }
}
