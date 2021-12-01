import Navbar from './Navbar/Navbar';

import './styles.css';

export default function Admin() {
  return (
    <div className="admin-container">
      <Navbar />
      <div className="admin-content">
        <h1>Conteúdo</h1>
      </div>
    </div>
  );
}
