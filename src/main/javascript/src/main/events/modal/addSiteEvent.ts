import { IElementEvent } from '../elementEvent';
import { Modal } from 'bootstrap';
import axios from 'axios';
import { showModal } from '../../screen/modal/index';

export default class AddSiteEvent implements IElementEvent {
  execute() {
    showModal('addSite', 'Add Site', [
      (modal: Modal, modalBody: HTMLElement) => this.modalAddSite(modal, modalBody),
    ]);
  }


  private modalAddSite(modal: Modal, modalBody: HTMLElement) {
    const link = (modalBody.getElementsByClassName('addSiteUrl')[0] as HTMLInputElement).value;
    const uri = new URL(window.location.href);
    axios
      .post(uri.origin + '/addFeed', {
        link: link,
      })
      .then((response) => {
        console.log(response.data);

        modal.hide();
      })
      .catch((error) => {
        console.error(error);
        modal.hide();
      });
  }
}
