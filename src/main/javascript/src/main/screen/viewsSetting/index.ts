import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from '@fortawesome/fontawesome-svg-core';
import {
  faBars,
  faCheck,
  faSyncAlt,
  faPlus,
  faWrench,
  faTh,
  faIdCard,
  faCogs,
  faStepBackward,
  faBookmark,
  faPlayCircle,
  faBackward,
  faSearch,
} from '@fortawesome/free-solid-svg-icons/index';

import { faYoutube } from '@fortawesome/free-brands-svg-icons';
import GaleWingApi from '../../api/galeWingApi';

library.add(
  faBars,
  faCheck,
  faSyncAlt,
  faPlus,
  faWrench,
  faTh,
  faIdCard,
  faCogs,
  faStepBackward,
  faBookmark,
  faPlayCircle,
  faBackward,
  faYoutube,
  faSearch,
);
dom.watch();

window.onload = async () => {

  const addBtn = document.getElementById("add");
  addBtn?.addEventListener("click", () => addView());

  const saveBtn = document.getElementById("save");
  saveBtn?.addEventListener("click", () => saveView());

  const viewOptions = document.getElementById("viewOptions");
  const viewNameEl = (<HTMLInputElement>document.getElementById("viewName"))!;

  console.log("init", viewNameEl)
  viewNameEl.addEventListener("change", () => {
    console.log("onchange", viewNameEl)
    const viewName = viewNameEl.value;

    if(!viewName) {
      return;
    }

    Array.from(viewOptions!.children)
      .filter(optionEl => optionEl) // 空文字除外
      .map(optionEl => <HTMLOptionElement>optionEl)
      .filter(optionEl => optionEl.value == viewName)
      .forEach(optionEl => setupBadge(optionEl));
  })

}

function setupBadge(optionEl: HTMLOptionElement) {
  // バッチの全削除
  const viewsSite = document.getElementById("views-site")!;
  while( viewsSite.firstChild ){
    viewsSite.removeChild( viewsSite.firstChild );
  };

  // バッチの付与
  const viewOptions = document.getElementById("viewOptions");
  const viewNameEl = (<HTMLInputElement>document.getElementById("viewName"))!;
  Array.from(viewOptions!.children)
    .filter(optionEl => optionEl)
    .map(optionEl => <HTMLOptionElement>optionEl)
    .filter(optionEl => optionEl.value == viewNameEl.value)
    .map(optionEl => optionEl.dataset.sitejson)
    .filter(siteList => siteList)
    .forEach(siteList => {
      const viewsSiteEl = document.getElementById("views-site");
      let json = JSON.parse(siteList!);

      json.forEach((element: { uuid: string; title: string; }) => {
        let badge = createBadge(element.uuid, element.title)
        viewsSiteEl!.appendChild(badge);
      });
    })
}

function addView() {
  const selectEl = <HTMLSelectElement>document.getElementById("siteSelect");
  const siteUuid = selectEl?.value;

  if(!siteUuid) {
    return
  }

  Array.from(selectEl.options).filter(siteInfo => siteInfo.value == siteUuid).forEach(siteInfo => {
    const badge = createBadge(siteInfo.value, siteInfo.textContent!);
    const viewsSiteEl = document.getElementById("views-site");
    viewsSiteEl?.appendChild(badge);
  });

}

function createBadge(uuid: string, title: string) {
  const spanEl = document.createElement("span");
  spanEl.classList.add("badge", "text-bg-primary");
  spanEl.textContent = title;
  spanEl.dataset.siteUuid = uuid;
  spanEl.classList.add("m-2")

  const closeBtnEl = document.createElement("button");
  closeBtnEl.classList.add("btn-close", "ms-2", "bottom-50", "end-50");
  closeBtnEl.ariaLabel = "Close";
  closeBtnEl.type = "button";
  closeBtnEl.addEventListener("click", () => {spanEl.remove()})

  spanEl.appendChild(closeBtnEl);
  return spanEl;
}

function saveView() {

  const badgeList = Array.from(document.getElementsByClassName("badge"));
  if(!badgeList) {
    return;
  }

  const viewName = (<HTMLInputElement>document.getElementById("viewName")).value;

  if(!viewName) {
    return;
  }

  const viewOptions = <HTMLDataListElement>document.getElementById("viewOptions");
  const viewId = Array.from(viewOptions.children)
    .map(optionEl => <HTMLOptionElement>optionEl)
    .find(optionEl => optionEl.innerText == viewName)?.value!;

  const siteUuidList = badgeList
    .map(badge => <HTMLSpanElement>badge)
    .map(badge => badge.dataset.siteUuid!);

  GaleWingApi.getInstance().viewSave(viewId, viewName, siteUuidList).then(() => location.reload())
}

