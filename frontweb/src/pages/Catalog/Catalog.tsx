import Navbar from 'components/Navbar/Navbar';
import ProductCard from 'components/ProductCard/ProductCard';

export default function Catalog() {
  return (
    <>
      <Navbar />
      <div className="container my-4">
        <div className="row">
          <div className="col-sm-6 col-lg-4 col-xl-3">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4 col-xl-3">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4 col-xl-3">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4 col-xl-3">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4 col-xl-3">
            <ProductCard />
          </div>
        </div>
      </div>
    </>
  );
}
