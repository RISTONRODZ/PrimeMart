import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './app/App.tsx'
import {BrowserRouter} from "react-router";
import ScrollToTop from "./features/ScrollToTop.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <BrowserRouter>
          <ScrollToTop />
          <App />
      </BrowserRouter>
  </StrictMode>,
)
