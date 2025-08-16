import axios from 'axios';
import { IElementEvent } from '../elementEvent';
import { showModal } from '../../screen/modal';
import { Modal } from 'bootstrap';

export default class ImportOpml implements IElementEvent {

  constructor() { }

  execute() {
    showModal('importOpml', 'Import OPML', [(modal: Modal, modalBody: HTMLElement) => this.importOpml()]);

  }

  importOpml(): void {
    let uri = new URL(window.location.href);
    let ajaxUrl = uri.origin + '/opml/import';

    let params = new FormData();
    let inputEl = document.getElementById('importOpmlFile') as HTMLInputElement;
    params.append('file', inputEl.files![0]);
    axios
      .post(ajaxUrl, params)
      .then((response) => { })
      .catch((error) => { });
  }
}
