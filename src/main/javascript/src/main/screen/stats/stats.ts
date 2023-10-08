import { Exception } from 'handlebars/runtime';
import GaleWingApi from '../../api/galeWingApi';
import 'bootstrap/dist/css/bootstrap.min.css';
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
  faListCheck,
  faLeftLong,
  faRightLong,
} from '@fortawesome/free-solid-svg-icons/index';

import { faYoutube } from '@fortawesome/free-brands-svg-icons';

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
  faListCheck,
  faLeftLong,
  faRightLong,
);
dom.watch();

import 'gridjs/dist/theme/mermaid.css';

import hideModifier from '@popperjs/core/lib/modifiers/hide';

document.addEventListener('DOMContentLoaded', async () => {
  let api = GaleWingApi.getInstance();

  let statsIdListRes = await api.getStatsIdList();

  let statsIdListData = statsIdListRes.data as Array<string>;

  // Load the Visualization API and the corechart package.
  google.charts.load('current', { packages: ['corechart'] });

  // Set a callback to run when the Google Visualization API is loaded.
  google.charts.setOnLoadCallback(drawChart);

  // Callback that creates and populates a data table,
  // instantiates the pie chart, passes in the data and
  // draws it.
  function drawChart(rows: any[]) {
    let statsList = document.getElementById('statsList');
    statsIdListData.forEach(async (value, index) => {
      let divEl = document.createElement('div');
      divEl.id = 'stats-' + index;
      statsList?.append(divEl);
      let executeStatsSqlRes = await api.executeStatsSql(value);

      console.log(executeStatsSqlRes);
      // Create the data table.
      let data = new google.visualization.DataTable();

      executeStatsSqlRes.data.columns.forEach((element: { name: string; type: string }) => {
        data.addColumn(element.type, element.name);
      });

      data.addRows(executeStatsSqlRes.data.values);

      // Set chart options
      let options = { title: executeStatsSqlRes.data.stats.title, width: 800, height: 600 };

      // Instantiate and draw our chart, passing in some options.
      let chart = createVisualize(executeStatsSqlRes.data.stats.visualize, divEl);
      chart.draw(data, options);
    });
  }

  function createVisualize(visualization: string, el: HTMLDivElement) {
    if (visualization == 'ColumnChart') {
      return new google.visualization.ColumnChart(el);
    }

    if (visualization == 'PieChart') {
      return new google.visualization.PieChart(el);
    }

    throw new Exception('no visualization');
  }
});
