const powerInput=document.getElementById("power");
const hoursInput=document.getElementById("hours");
const preview=document.getElementById("energy-preview");
function updateEnergyPreview(){
  const watts=Number(powerInput.value),hours=Number(hoursInput.value);
  if(!powerInput.value||!hoursInput.value){preview.innerHTML="<b>How energy is estimated</b><div>Daily energy (kWh) = power (W) × daily use (hours) ÷ 1,000</div><small>Enter power and hours to preview the estimate.</small>";return;}
  const daily=watts*hours/1000,rate=dashboard.settings?.electricityRate||8;
  preview.innerHTML="<b>Estimated energy and cost</b><div>"+watts+" W × "+hours+" h ÷ 1,000 = <strong>"+daily.toFixed(2)+" kWh/day</strong></div><small>Monthly estimate: "+(daily*30).toFixed(2)+" kWh · Daily cost: ₹"+(daily*rate).toFixed(2)+" at ₹"+rate+"/kWh</small>";
}
powerInput.addEventListener("input",updateEnergyPreview);hoursInput.addEventListener("input",updateEnergyPreview);
const existingOpenModal=window.openModal;window.openModal=function(appliance){existingOpenModal(appliance);updateEnergyPreview();};

const resetButton=document.createElement("button");
resetButton.type="button";
resetButton.textContent="Reset data";
resetButton.style.cssText="border:1px solid #edc8c5;background:#fff;color:#a33f37;border-radius:8px;padding:11px 14px;font:600 12px 'DM Sans',sans-serif;cursor:pointer";
document.querySelector(".header-right").prepend(resetButton);
resetButton.addEventListener("click",async()=>{
  if(!confirm("Reset all household data? This permanently deletes every appliance, schedule, and usage record, and restores default preferences."))return;
  resetButton.disabled=true;
  try{
    const response=await fetch("/api/reset",{method:"POST"});
    if(!response.ok)throw new Error("Reset failed ("+response.status+").");
    location.reload();
  }catch(error){alert(error.message||"Could not reset household data.");resetButton.disabled=false;}
});
