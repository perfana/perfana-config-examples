import {ReactComponent as Logo} from "../asset/perfana-logo.svg"
import styles from './header.module.css'
import {Divider} from 'antd';

export const Header = () => {

    return (
        <div>
            <div className={styles.container}>
                <div className={styles.logo}><Logo/></div>

            </div>
            <Divider/>
        </div>
    )
}