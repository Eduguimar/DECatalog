import { Route, Switch } from 'react-router';
import Navbar from './Navbar/Navbar';

import './styles.css';

export default function Admin() {
  return (
    <div className="admin-container">
      <Navbar />
      <div className="admin-content">
        <Switch>
          <Route path="/admin/products">
            <h1>Product CRUD</h1>
          </Route>
          <Route path="/admin/categories">
            <h1>Category CRUD</h1>
          </Route>
          <Route path="/admin/users">
            <h1>User CRUD</h1>
          </Route>
        </Switch>
      </div>
    </div>
  );
}
