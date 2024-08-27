package com.digital.paisalo.jlgsourcing.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.digital.paisalo.jlgsourcing.DbIGL;

import java.io.Serializable;

@ModelContainer
@Table(database = DbIGL.class)
public class ESignGuarantor extends BaseModel implements Serializable {
    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public int FiCode;

    @Column
    public String Creator;

    @Column
    public int GuarantorNo;

    @Column
    public String GuarantorName;

    @Column
    public String GuarantorFather;

    @Column
    public String GuarantorAadharNo;

    @Column
    public String Address;

    @Column
    public String MobileNo;

    @Column
    public String FoCode;

    @Column
    public String ESignSucceed;

    @Column
    public String KycUuid;

    public ESigner getESigner() {
        ESigner eSigner = new ESigner();
        eSigner.AadharNo = GuarantorAadharNo;
        eSigner.Name = GuarantorName;
        eSigner.MobileNo = MobileNo;
        eSigner.FiCode = FiCode;
        eSigner.Creator = Creator;
        eSigner.Address = Address;
        eSigner.GrNo = GuarantorNo;
        eSigner.ESignerType = "Guarantor";
        eSigner.ESignSucceed = ESignSucceed;
        eSigner.FatherName = GuarantorFather;
        eSigner.KycUuid = KycUuid;
        return eSigner;
    }
}
