import axios from 'axios';
import GaleWingModal from '../galeWingModal';
import { IElementEvent } from './elementEvent';

export default class ImportOpml implements IElementEvent {
  private modal: GaleWingModal;

  constructor() {}

  execute() {
    this.modal = new GaleWingModal();
    this.modal.setBodyClone('modalImportOpmlBody');
    this.modal.setFooterClone('modalImportOpmlFooter');
    this.modal.setModalSubmit(this.importOpml);

    this.modal.show();
  }

  importOpml(event: Event): void {
    var uri = new URL(window.location.href);
    var ajaxUrl = uri.origin + '/opml/import';

    var params = new FormData();
    var inputEl = document.getElementById('importOpmlFile') as HTMLInputElement;
    params.append('file', inputEl.files[0]);
    axios
      .post(ajaxUrl, params)
      .then((response) => {})
      .catch((error) => {});
  }
}
