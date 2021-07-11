import { ReactComponent as MainImage } from 'assets/images/main-img.svg';
import ButtonIcon from 'components/ButtonIcon/ButtonIcon';
import Navbar from 'components/Navbar/Navbar';

import './styles.css';

export default function Home() {
  return (
    <>
      <Navbar />
      <div className="home-container">
        <div className="home-card">
          <div className="home-content-container">
            <div>
              <h1>Conheça o nosso catálogo de produtos</h1>
              <p>
                Ajudaremos você a encontrar os melhores produtos disponíveis no
                mercado.
              </p>
            </div>
            <ButtonIcon />
          </div>
          <div className="home-image-container">
            <MainImage />
          </div>
        </div>
      </div>
    </>
  );
}
