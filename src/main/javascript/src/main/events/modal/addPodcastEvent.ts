import { IElementEvent } from '../elementEvent';
import { Modal } from 'bootstrap';
import { showModal } from '../../screen/modal/index';
import GaleWingApi from '../../api/galeWingApi';

export default class AddPodcastEvent implements IElementEvent {
  execute() {
    showModal('addPodcast', 'Add Podcast', [
      (modal: Modal, modalBody: HTMLElement) => this.modalPodcastSite(modal, modalBody),
    ]);
  }


  private modalPodcastSite(modal: Modal, modalBody: HTMLElement) {
    const url = (modalBody.getElementsByClassName("podcastUrl")[0] as HTMLInputElement).value;
    const title = (modalBody.getElementsByClassName("podcastTitle")[0] as HTMLInputElement).value;

    GaleWingApi.getInstance().podcastAdd(url, title).catch(() => {
      alert('Failed to add podcast.');
    }).finally(() => {
      modal.hide();
    });
  }
}
