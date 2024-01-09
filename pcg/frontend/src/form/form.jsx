import {Button, Input, Radio, Space} from "antd";
import {useState} from "react";
import styles from "./form.module.css";
import axios from "axios";

export const Form = () =>{

    const [value, setValue] = useState(1);
    const [projectName, setProjectName] = useState(null);

    const onChange = (e) => {
        console.log('radio checked', e.target.value);
        setValue(e.target.value);
    };
    const onClick = async () =>{
        await axios.get(`/download/${projectName}`)
    }
    return (
        <div className={styles.container}>
            <Input onChange={setProjectName}/>
            <Radio.Group onChange={onChange} value={value}>
                <Space direction="vertical">
                    <Radio value={1}>Option A</Radio>
                    <Radio value={2}>Option B</Radio>
                    <Radio value={3}>Option C</Radio>
                </Space>
            </Radio.Group>
            <Button type="primary" onClick={onClick}>Primary Button</Button>
        </div>
    )
}