
function loadDistrict(provinceId) {

    const unitSelect = document.getElementById("administrativeUnitId");
    let htmlDistrictOption;
    if(provinceId === null || provinceId === "") {
        htmlDistrictOption += '<option value="" disabled selected>Chọn quận/huyện</option>';
        unitSelect.innerHTML = htmlDistrictOption;
        return;
    }
    axios.get(`/api/recruiter/load-district?province_id=${provinceId}`)
        .then(response =>{
            const data = response.data;
            htmlDistrictOption = '<option value="" disabled selected>Chọn quận/huyện</option>';
            data.forEach(district => {
                htmlDistrictOption += `<option value="${district.unitId}">${district.unitName}</option>`;
            })
            unitSelect.innerHTML = htmlDistrictOption;
        })
        .catch(error => {
            console.log("Không tìm được mã tỉnh!", error);
        })
}