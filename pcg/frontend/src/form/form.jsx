import {Button, Input, Radio, Space} from "antd";
import {useState} from "react";
import styles from "./form.module.css";
import axios from "axios";

export const Form = () =>{

    const [perfanaClient, setPerfanaClient] = useState(null);
    const [perfanaAPIKey, setPerfanaAPIKey] = useState(null);
    const [publicKeyCertificate, setPublicKeyCertificate] = useState(null);
    const [privateKey, setPrivateKey] = useState(null);
    const [value, setValue] = useState(1);



    const onChange = (e) => {
        console.log('radio checked', e.target.value);
        setValue(e.target.value);
    };
    const onClick = async () =>{
        await axios.get(`/download/${perfanaClient}`)
    }
    return (
        <div className={styles.container}>
            <Input onChange={setPerfanaClient}/>
            <Radio.Group onChange={onChange} value={value}>
                <Space direction="vertical">
                    <Radio value={1}>Gatling</Radio>
                    <Radio value={2}>JMeter</Radio>
                    <Radio value={3}>K6</Radio>
                </Space>
            </Radio.Group>
            <Input onChange={setPerfanaAPIKey}/>
            <Input onChange={setPublicKeyCertificate}/>
            <Input onChange={setPrivateKey}/>

            <Button type="primary" onClick={onClick}>Primary Button</Button>
        </div>
    )
}