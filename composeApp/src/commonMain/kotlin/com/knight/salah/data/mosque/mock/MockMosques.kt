package com.knight.salah.data.mosque.mock

import com.knight.salah.data.mosque.MosqueApi
import com.knight.salah.domain.model.mosque.AwqatMosque
import kotlinx.serialization.json.Json


class MockMosqueApi : MosqueApi {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        private val mosques: List<AwqatMosque> =
            json.decodeFromString(mosquesJson)
    }

    override suspend fun getMosques(): List<AwqatMosque> {
        return mosques
    }
}

private val mosquesJson = """
[
    {
        "id": "b5e1a759-5980-4347-be43-8723e3dcaa72",
        "name": "Abu Bakr Islamic Centre",
        "address": " 7375 144 Street",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.11781,
        "longitude": -122.823226,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "42a4f039-5e07-4241-a8ab-e067199cf5fe",
        "name": "Ajyal Islamic Center",
        "address": "181 Keefer Street, Unit 202",
        "city": "Vancouver",
        "province_state": "BC",
        "latitude": 49.2801009819518,
        "longitude": -123.108555942966,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "fa41d767-cac8-49f4-89b3-2073f224d0f7",
        "name": "Al Ihsan Islamic Centre",
        "address": "2701B Esplanade Street",
        "city": "Port Moody",
        "province_state": "British Columbia",
        "latitude": 49.279353,
        "longitude": -122.852243,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "1c6309c5-5dda-4f37-9cbe-d9801190da4d",
        "name": "Al Iman Islamic Centre",
        "address": "18-13478 78 Ave",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.144521,
        "longitude": -122.851278,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "979b2ff1-f133-443a-a808-e05ad4273a14",
        "name": "Al Iman Metrotown Masjid",
        "address": "204-7060 Waltham Ave",
        "city": "Burnaby",
        "province_state": "British Columbia",
        "latitude": 49.219872,
        "longitude": -122.976119,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "dba7e795-3213-44a1-b8b6-c0c6d49f4126",
        "name": "Al Masjid Al Jamia",
        "address": " 655 W. 8th Avenue",
        "city": "Vancouver",
        "province_state": "British Columbia",
        "latitude": 49.26444,
        "longitude": -123.118972,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "a71534e1-caa0-429a-a5c3-e102b4bdce0d",
        "name": "Al-Rauf Education and Welfare Foundation",
        "address": "3989 Henning Drive",
        "city": "Burnaby",
        "province_state": "BC",
        "latitude": 49.264108,
        "longitude": -123.01741,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "5b885cd4-2a00-4c41-9593-9335fc44a457",
        "name": "Amir Hamza Musalla",
        "address": "9250 Scott Rd",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.184926,
        "longitude": -122.891012,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "451caf35-bdf6-4a06-b9c7-d748594a68f2",
        "name": "Baitul Mukarram Islamic Society",
        "address": " 6409 Arbroath St",
        "city": "Burnaby",
        "province_state": "British Columbia",
        "latitude": 49.218737,
        "longitude": -122.970363,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "0712ba08-ad7c-45f3-8fe3-caaae76dbf4d",
        "name": "BCMA Richmond Jamea Masjid",
        "address": "12300 Blundell Rd",
        "city": "Richmond",
        "province_state": "British Columbia",
        "latitude": 49.153186,
        "longitude": -123.087719,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "aee6c992-6831-41c8-9b56-2ca5af4d28ec",
        "name": "Bilal Masjid",
        "address": "7726 Edmonds St",
        "city": "Burnaby",
        "province_state": "British Columbia",
        "latitude": 49.224171,
        "longitude": -122.940149,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "4f48529d-17b9-471a-a362-33d94ea20e4c",
        "name": "Brentwood BNH ** JUMA ONLY **",
        "address": "2055 Rosser Avenue",
        "city": "Burnaby",
        "province_state": "British Columbia",
        "latitude": 49.2661619847627,
        "longitude": -123.006358993736,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "1616938b-4503-4f9a-8d36-2dc6f5419d85",
        "name": "Cloverdale Islamic Society",
        "address": "17665 66A Ave",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.123827,
        "longitude": -122.733699,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "5a7c9ed9-23f9-4ea2-8503-0944ade1ea5d",
        "name": "Coquitlam Islamic Centre",
        "address": "202-504 Cottonwood Ave",
        "city": "Coquitlam",
        "province_state": "British Columbia",
        "latitude": 49.258509637547,
        "longitude": -122.892672197799,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "55e77293-56c3-45c2-a6a8-ba374b5eabbb",
        "name": "Faizan-e-Madina Islamic Center",
        "address": "7062 134 St",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.130985,
        "longitude": -122.850896,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "65059b19-8a2c-4aa2-b40b-d26696fa827c",
        "name": "Fiji Islamic Centre",
        "address": "12988 84 Ave",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.154944,
        "longitude": -122.862867,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "3fc905d5-5fc3-4d74-975c-f167c425e315",
        "name": "Fleetwood Islamic Academy Society of BC",
        "address": " Unit 209 - 210, 8462 162 Street",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.1565745243097,
        "longitude": -122.771816927287,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "2a182e04-7959-4b22-b8a1-4b575c8c825c",
        "name": "Fort St John Musallah",
        "address": " 9715 102nd Street",
        "city": "Fort St. John",
        "province_state": "British Columbia",
        "latitude": 56.244199,
        "longitude": -120.850608,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "eae4e528-be10-493f-abe6-b96f0125a74a",
        "name": "Granville musallah ",
        "address": "695 Smithe St Vancouver BC V6B 2C9 Canada",
        "city": "Vancouver",
        "province_state": "BC",
        "latitude": 49.280249,
        "longitude": -123.120953,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "558e3a10-99c3-49e8-8fa9-0c6d8fc5e075",
        "name": "Islamic Society of Ridge Meadows",
        "address": "21991 Cliff Ave",
        "city": "Maple Ridge",
        "province_state": "British Columbia",
        "latitude": 49.216222,
        "longitude": -122.612884,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "b425ec51-10cd-4c63-8047-c85358f16d71",
        "name": "MAC Vancouver",
        "address": " 2122 Kingsway",
        "city": "Vancouver",
        "province_state": "British Columbia",
        "latitude": 49.244008,
        "longitude": -123.063491,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "6d454cce-b747-4176-8540-dea7c328fadc",
        "name": "Marpole Musallah",
        "address": "8879 Selkirk St",
        "city": "Vancouver",
        "province_state": "British Columbia",
        "latitude": 49.2045756341235,
        "longitude": -123.133633282007,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "a5482569-9b79-47c8-b2de-4fda4ac81931",
        "name": "Masjid Al Huda",
        "address": " 14136 Grosvenor Road",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.20703,
        "longitude": -122.830149,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "09f21b0e-64a6-493f-8e45-e713b05a917b",
        "name": "Masjid Al Noor",
        "address": " 13526 98A Avenue",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.181367,
        "longitude": -122.847669,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "30095751-d33a-4daa-963b-a56d38cdb261",
        "name": "Masjid Al Rahmah",
        "address": "13585 62 Ave",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.115618,
        "longitude": -122.845774,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "fede16e1-7b38-4abc-8473-2a832651a334",
        "name": "Masjid Al Sahaba",
        "address": "5768 203 St",
        "city": "Langley",
        "province_state": "British Columbia",
        "latitude": 49.1073889513203,
        "longitude": -122.659695243919,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "f267c0e2-edf6-4c01-86cf-2cffdea6c6df",
        "name": "Masjid Al-Salaam",
        "address": "5060 Canada Way",
        "city": "Burnaby",
        "province_state": "British Columbia",
        "latitude": 49.2399085894181,
        "longitude": -122.964185864278,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "8f21a37a-a674-4e8d-bfbf-6626d355ddcd",
        "name": "Masjid Anwar-e-Madina",
        "address": "13560 105A Ave",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.194345,
        "longitude": -122.846486,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "e7bfc68e-552f-4921-851a-cf64127aec31",
        "name": "Masjid Ar-Rahman North Vancouver",
        "address": "1398 W. 15th St.",
        "city": "North Vancouver",
        "province_state": "British Columbia",
        "latitude": 49.3223397009782,
        "longitude": -123.11297100124,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "96ac3382-aef7-4710-a187-7002ba7f4323",
        "name": "Masjid Guildford",
        "address": "15290 103A Ave #101",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.1900649734629,
        "longitude": -122.798232512977,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "28e9a078-c04d-43d6-a4de-3037697c34ab",
        "name": "Masjid Omar Al-Farooq",
        "address": "1659 E 10th Ave",
        "city": "Vancouver",
        "province_state": "BC",
        "latitude": 49.261652,
        "longitude": -123.070574,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "8fe9f823-98ff-4304-a312-337d34e3620a",
        "name": "Masjid Ul Haqq",
        "address": "4162 Welwyn Street",
        "city": "Vancouver",
        "province_state": "British Columbia",
        "latitude": 49.247926,
        "longitude": -123.069528,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "8ed441dc-0d3f-45b8-860f-2032a10f334f",
        "name": "MSA UBC",
        "address": "6174 University Blvd",
        "city": "Vancouver",
        "province_state": "BC",
        "latitude": 49.2652800406816,
        "longitude": -123.249507669721,
        "type": "msa",
        "is_active": true
    },
    {
        "id": "a71546cc-d9c5-4441-9407-0d286992c44f",
        "name": "North Delta Islamic Centre",
        "address": "11146 84 Ave",
        "city": "Delta",
        "province_state": "British Columbia",
        "latitude": 49.155793,
        "longitude": -122.912847,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "01b11b55-7156-4559-a05c-e1534d99d943",
        "name": "Surrey Jamea Masjid",
        "address": "12407 72nd Ave",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.13411,
        "longitude": -122.878576,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "20f192db-eedf-4440-9a9d-dafd70ccafb3",
        "name": "Taiba Musallah ",
        "address": " 1206 Kingston Street",
        "city": "New Westminster",
        "province_state": "British Columbia",
        "latitude": 49.2098432329249,
        "longitude": -122.934468670895,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "a585e908-ee30-4e62-932e-d11cb1f68529",
        "name": "Test Masjid",
        "address": "2580 McGill St",
        "city": "Vancouver",
        "province_state": "BC",
        "latitude": 49.288405,
        "longitude": -123.052632,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "069771aa-89ce-4533-a9b5-18c28e1ec187",
        "name": "West End Mussalah",
        "address": "708 Denman St ",
        "city": "Vancouver",
        "province_state": "British Columbia",
        "latitude": 49.291863,
        "longitude": -123.134408,
        "type": "masjid",
        "is_active": true
    },
    {
        "id": "e2fb4334-c87a-4372-9761-8a06627305c2",
        "name": "White Rock Muslim Association",
        "address": "15515 24 Ave #61",
        "city": "Surrey",
        "province_state": "British Columbia",
        "latitude": 49.0467305333655,
        "longitude": -122.793498673253,
        "type": "masjid",
        "is_active": true
    }
]
""".trimIndent()