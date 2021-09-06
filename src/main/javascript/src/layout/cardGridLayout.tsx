import * as React from 'react';
import * as ReactDOM from 'react-dom';

const imgStyle = {
  width: "18rem"
}

export default function init(count: number) {
  const CardGridLayout: React.FunctionComponent<{ compiler: string, framework: string }> = (props) => {
    return (
      <div>
        <div>{props.compiler}</div>
        <div>{props.framework}</div>
        <div className="row row-cols-2">
        {
          Array(count).fill('test').map((_, i) => {
            console.log(i)
            return (
              <div className="card" style={imgStyle}>
              <img src="" className="card-img-top" alt="..."></img>
              <div className="card-body">
                <h5 className="card-title">Card title</h5>
                <p className="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
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

