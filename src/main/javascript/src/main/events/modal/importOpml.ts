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
    const uri = new URL(window.location.href);
    const ajaxUrl = uri.origin + '/opml/import';

    const params = new FormData();
    const inputEl = document.getElementById('importOpmlFile') as HTMLInputElement;
    params.append('file', inputEl.files![0]);
    axios
      .post(ajaxUrl, params)
      .then((response) => { })
      .catch((error) => { });
  }
}
