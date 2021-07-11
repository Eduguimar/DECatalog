import Navbar from 'components/Navbar/Navbar';
import ProductCard from 'components/ProductCard/ProductCard';

export default function Catalog() {
  return (
    <>
      <Navbar />
      <div className="container my-4">
        <ProductCard />
      </div>
    </>
  );
}
