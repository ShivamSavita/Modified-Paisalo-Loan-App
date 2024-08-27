package com.digital.paisalo.jlgsourcing.entities;


import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@ModelContainer
    public class FiDocGeoLoc extends BaseModel implements Serializable {
        @Expose
        @Column
        public long FiCode;
        @Expose
        @Column
        public String Creator;
        @Expose
        @Column
        public String IsAadhaarEntry;
        @Expose
        @Column
        public String IsNameVerify;
        @Expose
        @Column
        public Float Aadhaar_Latitude_front;
        @Expose
        @Column
        public Float Aadhaar_Longitude_front;
        @Expose
        @Column
        public Float Aadhaar_Latitude_Back;
        @Expose
        @Column
        public Float Aadhaar_Longitude_Back;
        @Expose
        @Column
        public Float Voterid_Latitude_front;
        @Expose
        @Column
        public Float Voterid_Longitude_front;
        @Expose
        @Column
        public Float Voterid_Latitude_Back;
        @Expose
        @Column
        public Float Voterid_Longitude_Back;
        @Expose
        @Column
        public Float Pan_Latitude_front;
        @Expose
        @Column
        public Float Pan_Longitude_front;
        @Expose
        @Column
        public Float PassBook_Latitude_front;
        @Expose
        @Column
        public Float PassBook_Longitude_front;
        @Expose
        @Column
        public Float PassBook_Latitude_Last;
        @Expose
        @Column
        public Float PassBook_Longitude_Last;


        public FiDocGeoLoc(long Code,String creator,String isAdhaarEntry ,String isNameMatched) {
            this.FiCode = Code;
            this.Creator = creator;
            this.IsAadhaarEntry=isAdhaarEntry;
            this.IsNameVerify=isNameMatched;
        }

        @Override
        public String toString() {
            return "FiDocGeoLoc{" +
                    "  FiCode=" + FiCode +
                    ", Creator='" + Creator + '\'' +
                    ", IsAadhaarEntry='" + IsAadhaarEntry + '\'' +
                    ", IsNameVerify='" + IsNameVerify + '\'' +
                    ", Aadhaar_Latitude_front='" + Aadhaar_Latitude_front +
                    ", Aadhaar_Longitude_front='" + Aadhaar_Longitude_front +
                    ", Aadhaar_Latitude_Back='" + Aadhaar_Latitude_Back +
                    ", Aadhaar_Longitude_Back='" + Aadhaar_Longitude_Back +
                    ", Voterid_Latitude_front='" + Voterid_Latitude_front +
                    ", Voterid_Longitude_front='" + Voterid_Longitude_front +
                    ", Voterid_Latitude_Back='" + Voterid_Latitude_Back +
                    ", Voterid_Longitude_Back='" + Voterid_Longitude_Back +
                    ", Pan_Latitude_front='" + Pan_Latitude_front +
                    ", Pan_Longitude_front='" + Pan_Longitude_front +
                    ", PassBook_Latitude_front='" + PassBook_Latitude_front +
                    ", PassBook_Longitude_front='" + PassBook_Longitude_front +
                    ", PassBook_Latitude_Last='" + PassBook_Latitude_Last +
                    ", PassBook_Longitude_Last='" + PassBook_Longitude_Last +
                    '}';
        }
//    Borrower borrower = SQLite.select()
//            .from(Borrower.class)
//            .where(Borrower_Table.FiID.eq(borrowerID))
//            .querySingle();
//
//
//            List<FiDocGeoLoc> getDataFromFiDocGepLoc(){
//                return SQLite.select()
//                        .from(FiDocGeoLoc.class)
//                        .where(FiDocGeoLoc_)
//            }
    }
