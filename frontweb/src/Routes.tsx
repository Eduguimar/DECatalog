import Navbar from 'components/Navbar/Navbar';
import Admin from 'pages/Admin/Admin';
import Catalog from 'pages/Catalog/Catalog';
import Home from 'pages/Home/Home';
import ProductDetails from 'pages/ProductDetails/ProductDetails';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';

export default function Routes() {
  return (
    <BrowserRouter>
      <Navbar />
      <Switch>
        <Route path="/" exact>
          <Home />
        </Route>
        <Route path="/products" exact>
          <Catalog />
        </Route>
        <Route path="/products/:productId">
          <ProductDetails />
        </Route>
        <Redirect from="/admin" to="/admin/products" exact />
        <Route path="/admin">
          <Admin />
        </Route>
      </Switch>
    </BrowserRouter>
  );
}
