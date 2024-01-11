import {Button, Input, Radio, Space, Spin, Typography} from "antd";
import {useState} from "react";
import styles from "./form.module.css";
import axios from "axios";
import JSZip from "jszip";
import { LoadingOutlined } from '@ant-design/icons';

const { TextArea } = Input;
const { Title } = Typography;

export const Form = () =>{

    const [perfanaClient, setPerfanaClient] = useState(null);
    const [perfanaAPIKey, setPerfanaAPIKey] = useState(null);
    const [publicKeyCertificate, setPublicKeyCertificate] = useState('');
    const [privateKey, setPrivateKey] = useState('');
    const [testToolIndex, setTestToolIndex] = useState(0);
    const [valid, isValid] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);
    const TEST_TOOLS = [
        {index:0, name:'Gatling',value:'gatling'},
        {index:1, name:'JMeter',value:'jmeter'},
        {index:2, name:'K6',value:'k6'},
        {index:3, name:'Loadrunner',value:'loadrunner'},
        {index:4, name:'Locust',value:'locust'},
    ]


    const onChange = (e) => {
        setTestToolIndex(e.target.value);
    };
    const setPerfanaClientHandler = (e) => {
        isValid(e.target.value?.length >0)
        setPerfanaClient(e.target.value)
    }
    const setPerfanaAPIKeyHandler = (e) => {
        setPerfanaAPIKey(e.target.value)
    }
    const onClick = async () =>{
        setIsLoading(true)
        const zip = new JSZip();
        zip.file("tls.crt",`${publicKeyCertificate}` );
        zip.file("tls.key",`${privateKey}`);
        zip.file("setup.sh",
            `export PERFANA_CLIENT=${perfanaClient} \nexport LOAD_TEST_TOOL=${TEST_TOOLS[testToolIndex].value} \nexport PERFANA_API_KEY=${perfanaAPIKey}`
        );

        zip.generateAsync({type:"blob"}).then(function(content) {
            const form = new FormData();
            form.append('file', content);

            axios.post('/upload', form).then(result =>{
                setIsError(false)
                setIsLoading(false)
                window.location = result.data.downloadUrl
            }).catch(()=>{
                setIsError(true)
                setIsLoading(false)
            })
        });
    }
    return isLoading ?
            <div className={styles.spinnerContainer}>
            <Spin style={{margin:'auto'}} indicator={<LoadingOutlined style={{fontSize:150, color:'#222a56'}} spin />}  />
            </div>:
        <div >
            {isError && <Title type="danger" level={5}>Something went wrong. Please try again later</Title>
            }
            <div>
                <Title level={5}>Perfana client</Title>
                <Input onChange={setPerfanaClientHandler} placeholder="Perfana client"/>
            </div>
            <div>
                <Title level={5}>Test tool</Title>
                <Radio.Group onChange={onChange} value={testToolIndex}>
                    <Space direction="vertical">
                        {TEST_TOOLS.map(item => <Radio key={item.index} value={item.index}>{item.name}</Radio>)}
                    </Space>
                </Radio.Group>
            </div>
            <div>
                <Title level={5}>Perfana API Key</Title>
                <Input onChange={setPerfanaAPIKeyHandler} placeholder="Perfana API Key"/>
            </div>
            <div>
                <Title level={5}>Public Key Certificate</Title>
                <TextArea
                    showCount
                    onChange={e => setPublicKeyCertificate(e.target.value)}
                    placeholder="Public Key Certificate"
                    style={{height: 120, resize: 'none'}}
                />
            </div>
            <div>
                <Title level={5}>Private Key</Title>
                <TextArea
                    showCount
                    onChange={e => setPrivateKey(e.target.value)}
                    placeholder="Private Key"
                    style={{height: 120, resize: 'none'}}
                />
            </div>
            <Button type="primary" onClick={onClick} disabled={!valid}
                    rootClassName={styles.button}>Generate</Button>

    </div>

}