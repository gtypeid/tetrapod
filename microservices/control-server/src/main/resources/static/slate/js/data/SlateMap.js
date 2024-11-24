import CommonThomas from "./thomas/CommonThomas.js";

const SlateMap = {
    "main-id" : "",
    "sub-id" : "",
    "resource-path" : "/resource",
    "widget-replace" : true,
    "mobile" : 376,
    "tablet" : 576,
    "end-point" : "",
    "computers" : "",

    getCommon : ()=>{
        const comThomas = new CommonThomas();
        return comThomas;
    }
};

export default SlateMap;