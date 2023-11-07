import React from 'react';
import ReactDOM from 'react-dom/client';
import * as tf from '@tensorflow/tfjs';
import App from './App';

import './main.css';

tf.env().set('WEBGL_CPU_FORWARD', false);

console.log('tf서버:', tf.getBackend());
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
