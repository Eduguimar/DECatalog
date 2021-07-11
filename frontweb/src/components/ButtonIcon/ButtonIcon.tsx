import './styles.css';
import { ReactComponent as ArrowIcon } from 'assets/images/arrow.svg';

export default function ButtonIcon() {
  return (
    <div className="btn-container">
      <button className="btn btn-primary">
        <h6>Inicie agora a sua busca</h6>
      </button>
      <div className="btn-icon-container">
        <ArrowIcon />
      </div>
    </div>
  );
}
