import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom, text } from '@fortawesome/fontawesome-svg-core';
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
import 'gridjs/dist/theme/mermaid.css';

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

const headerConfig = [{id: "chk", text: "", type: "checkbox"}, {id:"uuid", text: "uuid", hidden: true}, {id: "title", text: "title"}];

window.addEventListener("DOMContentLoaded", () => {

  createTable(headerConfig, [], document.getElementById("wrapper"));


  const siteSelect = document.getElementById("siteSelect");
  siteSelect?.addEventListener("change", async(event) => {
    const viewId = (<HTMLSelectElement>event.target).value;

    let api = GaleWingApi.getInstance();
    let res = await api.getViewSiteList(viewId);

    removeAllChildNodes(document.getElementById("wrapper")!);

    createTable(headerConfig, res.data, document.getElementById("wrapper"));
  });

  const saveBtn = document.getElementById("save");
  saveBtn?.addEventListener("click", () => saveView());

  const newBtn = document.getElementById("new");
  newBtn?.addEventListener("click", () => saveNew());

});

function saveView() {

  const viewName = (<HTMLInputElement>document.getElementById("viewName")).value;

  const viewOptions = <HTMLSelectElement>document.getElementById("siteSelect");
  const selectedViewOption = viewOptions.options[viewOptions.selectedIndex];

  if(!selectedViewOption.value) {
    return;
  }

  update(selectedViewOption.value, selectedViewOption.text);

}

function saveNew(): any {
  const viewName = (<HTMLInputElement>document.getElementById("viewName")).value;

  if(!viewName) {
    return;
  }

  update("", viewName);
}

function update(viewId:string, viewName:string): void {
  if(!viewName) {
    return;
  }

  const data = convertTableToJson(headerConfig, document.getElementById("wrapper")?.firstChild as HTMLTableElement);

  const siteUuidList = data.filter((row: any) => row.chk)
    .map((row: any) => (<any>row)['uuid']);

  console.log(siteUuidList);

  GaleWingApi.getInstance().viewSave(viewId, viewName, siteUuidList).then(() => location.reload());
}


function createTable(hederConfigs: any[], data: any[], parent: HTMLElement | null) {

  const table = document.createElement('table');

  const tr = document.createElement('tr');
  hederConfigs.forEach((config: HeaderConfig) => {

    const th = document.createElement('th');

    th.innerText = config.text;
    th.style.display = config.hidden ? 'none' : '';
    tr.appendChild(th);
  });

  table.appendChild(tr);

  data.forEach((rowData: []) => {
    table.appendChild(createRow(hederConfigs, rowData));
  });

  parent?.appendChild(table);
}

function createRow(hederConfig: any[], rowData: any) {
  const tr = document.createElement('tr');

  hederConfig.forEach((headerConfig: HeaderConfig) => {
    const td = document.createElement('td');

    td.style.display = headerConfig.hidden ? 'none' : '';

    if(headerConfig.type === "checkbox") {
      const inputEl = document.createElement('input');
      inputEl.type = "checkbox";
      inputEl.checked = rowData[headerConfig.id];
      td.appendChild(inputEl);
    } else {
      if(rowData[headerConfig.id]) {
        td.innerText = rowData[headerConfig.id]
      }
    }
    tr.appendChild(td);
  })

  return tr;
}

function removeAllChildNodes(parent: HTMLElement) {
  while (parent.firstChild) {
      parent.removeChild(parent.firstChild);
  }
}

function convertTableToJson(headerConfig : HeaderConfig[], table: HTMLTableElement) {
  let json = [];

  for(var rowIndex = 0; rowIndex < table.rows.length; rowIndex++) {
    const row = table.rows[rowIndex];
    let rowData:any = {};

    for(var cellIndex = 0; cellIndex < headerConfig.length; cellIndex++) {
      const cell = row.cells[cellIndex];
      const id = headerConfig[cellIndex].id;

      rowData[id] = getCellValue(headerConfig[cellIndex], cell);
    }

    json.push(rowData);
  }

  return json;
}

function getCellValue(config: HeaderConfig, cell: HTMLTableCellElement) {
  if(config.type === "checkbox") {
    return (<HTMLInputElement>cell?.firstChild)?.checked;
  } else {
    return cell.innerText;
  }
}

interface HeaderConfig {
  id: string;
  text: string;
  hidden?: boolean;
  type?: string;
}
