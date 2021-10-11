import { Modal } from 'bootstrap';

export default class GaleWingModal {
  private modal: Modal;
  private modalBody: HTMLElement;
  private modalFooter: HTMLElement;
  private submitFunc: (event: Event) => void;

  constructor() {
    this.modalBody = document.getElementById('modal-body');
    this.modalFooter = document.getElementById('modal-footer');
    this.modal = new Modal(document.getElementById('exampleModal'));

    this.modal;
  }

  setBodyClone(bodyElementId: string) {
    var body = document.getElementById(bodyElementId);

    if (!body) {
      throw new ReferenceError(`not found element. id:${bodyElementId}`);
    }

    this.modalBody.appendChild(body.cloneNode(true));
  }

  setFooterClone(footerElementId: string) {
    var footer = document.getElementById(footerElementId);
    if (!footer) {
      throw new ReferenceError(`not found element. id:${footerElementId}`);
    }

    this.modalFooter.appendChild(footer.cloneNode(true));
  }

  setModalSubmit(callback?: (event: Event) => void) {
    var submit = this.modalFooter.getElementsByClassName('modal-submit');
    var submitEl = submit ? submit[0] : null;

    this.submitFunc = (e: Event) => {
      callback(e);
      this.modal.hide();
    };

    if (submitEl) {
      submitEl.addEventListener('click', this.submitFunc, { once: true });
    }
  }

  show() {
    if (!this.submitFunc) {
      this.setModalSubmit(() => {
        this.modal.hide();
      });
    }

    document.getElementById('exampleModal').addEventListener(
      'hidden.bs.modal',
      (event) => {
        var modalBody = document.getElementById('modal-body');
        while (modalBody.firstChild) {
          modalBody.removeChild(modalBody.firstChild);
        }

        var modalFooter = document.getElementById('modal-footer');
        while (modalFooter.firstChild) {
          modalFooter.removeChild(modalFooter.firstChild);
        }

        this.modal.dispose();
      },
      { once: true },
    );

    this.modal.show();
  }
}
