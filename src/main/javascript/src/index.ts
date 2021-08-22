import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { faBars } from "@fortawesome/free-solid-svg-icons/faBars";

library.add(faBars);
dom.watch();

import { Grid, html } from "gridjs";
import "gridjs/dist/theme/mermaid.css";

import hideModifier from '@popperjs/core/lib/modifiers/hide';

console.log("load")

window.onload = function() {

  console.log("onload")
  var uri = new URL(window.location.href);
  var ajaxUrl = uri.origin + "/feedlist" + location.search;

  console.log("ajaxUrl:",ajaxUrl);
  new Grid({
    columns: [
      {
        name: 'title',
        hidden: false,
        formatter: (cell, row) => html(`<a href='${row.cells[1].data}' target="_blank" rel="noopener">${row.cells[0].data}</a>`)
      },
      { name: 'link', hidden: true},
      { name: 'uri', hidden: true},
      { name: 'type', hidden: true},
      { name: 'author', hidden: true},
      { name: 'comments', hidden: true},
      { name: 'publishedDate', hidden: false}],
    pagination: true,
    sort: true,
    server: {
      url: ajaxUrl,
      then: data => {
        return data;
      }
    }
  }).render(document.getElementById("wrapper"))
};

export default { hideModifier }

