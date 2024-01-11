import { Form} from './form/form'
import {Header} from "./header/header";
import {Layout} from "./layout/layout";
import styles from './App.module.css'
function App() {
  return (
      <div className={styles.container}>
      <Layout >
          <Header />
          <Form />
      </Layout>
      </div>
  );
}

export default App;
