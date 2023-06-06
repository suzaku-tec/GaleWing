import { Modal } from 'bootstrap';
import { IElementEvent } from '../elementEvent';

export default class ConfirmModalEvent {
  private modal: Modal;

  constructor(msg: string, execute: () => void) {
    let div = document.createElement('div');
    div.innerText = msg;

    let modalBody = document.getElementById('modal-body')!;
    modalBody.appendChild(div);

    let confirmFooter = document.getElementsByClassName('confirmFooter')[0];
    let footer = this.initFooter(confirmFooter, execute);

    let modalHeaderCloseBtn = document.getElementById('modal-header-btn-close')!;
    modalHeaderCloseBtn.style.display = 'none';

    document.getElementById('exampleModal')!.addEventListener(
      'hidden.bs.modal',
      (event) => {
        this.disposeModal(modalBody, footer, modalHeaderCloseBtn);
      },
      { once: true },
    );

    this.modal = new Modal(document.getElementById('exampleModal')!, {
      backdrop: 'static',
    });
    this.modal.show();
  }

  private disposeModal(
    modalBody: HTMLElement,
    footer: HTMLElement,
    modalHeaderCloseBtn: HTMLElement,
  ) {
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
  }

  private initFooter(confirmFooter: Element, execute: () => void) {
    let footer = document.getElementById('modal-footer')!;
    footer.appendChild(confirmFooter.cloneNode(true));
    footer
      .getElementsByClassName('confirmOk')!
      .item(0)!
      .addEventListener('click', () => {
        execute();

        if (this.modal) {
          this.modal.hide();
        }
      });
    return footer;
  }

  close() {
    if (this.modal) {
      this.modal.hide();
      document.getElementById('modal-dialog')!.classList.remove('modal-dialog-centered');
    }
  }
}
