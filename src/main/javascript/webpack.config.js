// Generated using webpack-cli https://github.com/webpack/webpack-cli

import { resolve as _resolve } from 'path';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// webpack.config.js
import Dotenv from 'dotenv-webpack';

const isProduction = process.env.NODE_ENV == 'production';

const stylesHandler = 'style-loader';

const config = {
  entry: {
    main: './src/main/screen/feed/index.ts',
    settings: './src/main/screen/setting/index.ts',
    siteManagement: './src/main/screen/site/index.ts',
    stack: './src/main/screen/stack/index.ts',
    analysis: './src/main/screen/analysis/index.ts',
    category: './src/main/screen/category/index.ts',
    siteCategory: './src/main/screen/siteCategory/index.ts',
    stats: './src/main/screen/stats/stats.ts',
    task: './src/main/screen/task/task.ts',
    cardLayout: './src/main/screen/layout/cardLayout.ts',
    circulation: './src/main/screen/circulation/index.ts',
    newsSummary: './src/main/screen/news/newsSummary.ts',
    functionCtrl: './src/main/screen/functionCtrl/index.ts',
    viewSetting: './src/main/screen/viewsSetting/index.ts',
    shortcutter: './src/main/screen/shortcutter.ts',
    galeWingModal: './src/main/screen/modal/index.ts',
    rssBridge: './src/main/screen/rssBridge/index.ts',
    statics: './src/main/screen/statistics/index.ts',
    podcast: './src/main/screen/podcast/index.ts',
    aiRecommend: './src/main/screen/aiRecommend/index.ts',
  },
  output: {
    path: _resolve(__dirname, 'dist'),
    filename: '[name].js',
    publicPath: '/',
    libraryExport: 'default',
    libraryTarget: 'umd',
    globalObject: 'this',
  },
  plugins: [
    // Add your plugins here
    // Learn more about plugins from https://webpack.js.org/configuration/plugins/
    new Dotenv(),
  ],
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/i,
        loader: 'ts-loader',
        exclude: ['/node_modules/'],
        options: {
          transpileOnly: true,
          configFile: process.argv.mode === 'production' ? 'tsconfig.json' : 'tsconfig.dev.json',
        },
      },
      {
        test: /\.css$/i,
        use: [stylesHandler, { loader: 'css-loader', options: { url: false } }],
        sideEffects: true, // production modeでもswiper-bundle.cssが使えるように
      },
      {
        test: /\.(eot|svg|ttf|woff|woff2|png|jpg|gif)$/i,
        type: 'asset',
      },

      // Add your rules for custom modules here
      // Learn more about loaders from https://webpack.js.org/loaders/
    ],
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js'],
  },
};

export default () => {
  if (isProduction) {
    config.mode = 'production';
  } else {
    config.mode = 'development';
  }
  return config;
};
