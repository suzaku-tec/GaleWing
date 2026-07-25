import { closeModal, showExecFuncModal, showModal } from "../../screen/modal";
import GaleWingApi from "../../api/galeWingApi";
import { IElementEvent } from "../elementEvent";
import { Modal } from "bootstrap";

export default class relationListEvent implements IElementEvent {
  execute(link: string): void {

    const api = GaleWingApi.getInstance();

    api.relationList(link)
      .then((res) => {
        const urls: { title: string, link: string }[] = res.data;
        showExecFuncModal('relationList', 'Relation List', (typeModalBody: HTMLElement) => {
          console.log('urls', urls);
          console.log('typeModalBody', typeModalBody);
          urls.forEach((item: { title: string, link: string }) => {
            const linkElement = document.createElement('a');
            linkElement.href = item.link;
            linkElement.textContent = item.title;
            linkElement.style.border = '5px';
            typeModalBody.appendChild(linkElement);
            typeModalBody.appendChild(document.createElement('br'));
          });
        }, [(modal: Modal, modalBody: HTMLElement) => {
          const urlElements = modalBody.getElementsByTagName('a');

          const urls = Array.from(urlElements).map((element: HTMLAnchorElement) => {
            return element.href;
          });

          api.readDispList(urls).then(() => {
            closeModal();
          })
        }, (modal: Modal, modalBody: HTMLElement) => {
          closeModal();
        }]);
      });
  }
}
