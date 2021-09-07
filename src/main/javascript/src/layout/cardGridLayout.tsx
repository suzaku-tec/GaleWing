import * as React from 'react';
import * as ReactDOM from 'react-dom';

export default function init(count: number, col: number, feedList: any[]) {
  const gridColClass = "row row-cols-" + col;
  const CardGridLayout: React.FunctionComponent<{ compiler: string, framework: string }> = (props) => {
    return (
      <div>
        <div className={gridColClass}>
        {
          feedList.slice(0, count).map((feed, i) => {
            return (
              <div className="col">
              <img src="" className="card-img-top" alt="..."></img>
              <div className="card-body">
                <h5 className="card-title">{feed.title}</h5>
              </div>
            </div>
            );
          })
        }
        </div>
      </div>
    );
  }

  ReactDOM.render(
    <CardGridLayout compiler="TypeScript" framework="React" />,
    document.getElementById("app")
  );
}

