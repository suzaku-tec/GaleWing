import { closeModal, showExecFuncModal, showModal } from "../../screen/modal";
import GaleWingApi from "../../api/galeWingApi";
import { IElementEvent } from "../elementEvent";
import { Modal } from "bootstrap";

export default class relationListEvent implements IElementEvent {
  execute(uuid: string): void {

    let api = GaleWingApi.getInstance();

    api.relationList(uuid)
      .then((res) => {
        showExecFuncModal('relationList', 'Relation List', (modalBody: HTMLElement) => {
          res.data.forEach((link: { title: string, link: string }) => {
            let linkElement = document.createElement('a');
            linkElement.href = link.link;
            linkElement.textContent = link.title;
            linkElement.style.border = '5px';
            modalBody.appendChild(linkElement);
            modalBody.appendChild(document.createElement('br'));
          });
        }, [(modal: Modal, modalBody: HTMLElement) => {
          let urls = res.data.forEach((link: { title: string, link: string }) => link.link);
          api.readDispList(urls).then(() => {
            closeModal();
          })
        }, (modal: Modal, modalBody: HTMLElement) => {
          closeModal();
        }]);
      });
  }
}
