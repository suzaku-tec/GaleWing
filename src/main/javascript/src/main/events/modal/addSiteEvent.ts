import { IElementEvent } from '../elementEvent';
import { Modal } from 'bootstrap';
import axios from 'axios';

export default class AddSiteEvent implements IElementEvent {
  execute() {
    let modalBody = document.getElementById('modal-body')!;
    let modalFooter = document.getElementById('modal-footer')!;

    let modalAddSiteBody = document.getElementById('modalAddSiteBody')!;
    let modalAddSiteFooter = document.getElementById('modalAddSiteFooter')!;

    modalBody.appendChild(modalAddSiteBody.cloneNode(true));
    modalFooter.appendChild(modalAddSiteFooter.cloneNode(true));

    let modal = new Modal(document.getElementById('exampleModal')!);
    modalFooter.getElementsByClassName('modal-submit')[0].addEventListener('click', () => {
      this.modalAddSite(modal, modalBody);
    });
    modal.show();

    document
      .getElementById('exampleModal')!
      .addEventListener('hidden.bs.modal', this.disposeModal(modal), { once: true });
  }

  private disposeModal(modal: Modal): EventListenerOrEventListenerObject {
    return (event) => {
      let modalBody = document.getElementById('modal-body')!;
      while (modalBody.firstChild) {
        modalBody.removeChild(modalBody.firstChild);
      }

      let modalFooter = document.getElementById('modal-footer')!;
      while (modalFooter.firstChild) {
        modalFooter.removeChild(modalFooter.firstChild);
      }

      modal.dispose();
    };
  }

  private modalAddSite(modal: Modal, modalBody: HTMLElement) {
    let link = (modalBody.getElementsByClassName('addSiteUrl')[0] as HTMLInputElement).value;
    let uri = new URL(window.location.href);
    axios
      .post(uri.origin + '/addFeed', {
        link: link,
      })
      .then((response) => {
        console.log(response.data);

        modal.hide();
      })
      .catch((error) => {
        modal.hide();
      });
  }
}
