import './styles.css';
import ProductPrice from 'components/ProductPrice/ProductPrice';
import { Product } from 'types/product';

type Props = {
  product: Product;
};

export default function ProductCard({ product }: Props) {
  return (
    <div className="base-card product-card">
      <div className="card-top-container">
        <img src={product.imgUrl} alt={product.name} />
      </div>
      <div className="card-bottom-container">
        <h6>{product.name}</h6>
        <ProductPrice price={product.price} />
      </div>
    </div>
  );
}
