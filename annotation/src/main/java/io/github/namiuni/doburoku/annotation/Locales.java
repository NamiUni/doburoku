/*
 * This file is part of doburoku, licensed under the MIT License.
 *
 * Copyright (c) 2025 Namiu (Unitarou)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.namiuni.doburoku.annotation;

import java.util.Locale;
import org.jspecify.annotations.NullMarked;

/**
 * Enumeration of supported locales used by annotation declarations.
 *
 * <p>Each constant wraps a corresponding {@link java.util.Locale} instance.</p>
 */
@NullMarked
public enum Locales {

    // Root locale
    ROOT(Locale.ROOT),

    // A-B
    AF_ZA(Locale.of("af", "ZA")), // Afrikaans
    AR_SA(Locale.of("ar", "SA")), // Arabic
    AST_ES(Locale.of("ast", "ES")), // Asturian
    AZ_AZ(Locale.of("az", "AZ")), // Azerbaijani
    BA_RU(Locale.of("ba", "RU")), // Bashkir
    BAR(Locale.of("bar", "")), // Bavarian
    BE_BY(Locale.of("be", "BY")), // Belarusian
    BG_BG(Locale.of("bg", "BG")), // Bulgarian
    BR_FR(Locale.of("br", "FR")), // Breton
    BRB(Locale.of("brb", "")), // Barbadian
    BS_BA(Locale.of("bs", "BA")), // Bosnian

    // C-D
    CA_ES(Locale.of("ca", "ES")), // Catalan
    CS_CZ(Locale.of("cs", "CZ")), // Czech
    CY_GB(Locale.of("cy", "GB")), // Welsh
    DA_DK(Locale.of("da", "DK")), // Danish
    DE_AT(Locale.of("de", "AT")), // German (Austria)
    DE_CH(Locale.of("de", "CH")), // German (Switzerland)
    DE_DE(Locale.GERMANY), // German (Germany)

    // E-F
    EL_GR(Locale.of("el", "GR")), // Greek
    EN_AU(Locale.of("en", "AU")), // English (Australia)
    EN_CA(Locale.CANADA), // English (Canada)
    EN_GB(Locale.UK), // English (United Kingdom)
    EN_NZ(Locale.of("en", "NZ")), // English (New Zealand)
    EN_PT(Locale.of("en", "PT")), // English (Pirate)
    EN_UD(Locale.of("en", "UD")), // English (Upside Down)
    EN_US(Locale.US), // English (United States)
    ENP(Locale.of("enp", "")), // English (Pirate)
    EO_UY(Locale.of("eo", "UY")), // Esperanto
    ES_AR(Locale.of("es", "AR")), // Spanish (Argentina)
    ES_CL(Locale.of("es", "CL")), // Spanish (Chile)
    ES_EC(Locale.of("es", "EC")), // Spanish (Ecuador)
    ES_ES(Locale.of("es", "ES")), // Spanish (Spain)
    ES_MX(Locale.of("es", "MX")), // Spanish (Mexico)
    ES_UY(Locale.of("es", "UY")), // Spanish (Uruguay)
    ES_VE(Locale.of("es", "VE")), // Spanish (Venezuela)
    ET_EE(Locale.of("et", "EE")), // Estonian
    EU_ES(Locale.of("eu", "ES")), // Basque
    FA_IR(Locale.of("fa", "IR")), // Persian
    FI_FI(Locale.of("fi", "FI")), // Finnish
    FIL_PH(Locale.of("fil", "PH")), // Filipino
    FO_FO(Locale.of("fo", "FO")), // Faroese
    FR_CA(Locale.CANADA_FRENCH), // French (Canada)
    FR_FR(Locale.FRANCE), // French (France)
    FRA_DE(Locale.of("fra", "DE")), // Franconian
    FUR_IT(Locale.of("fur", "IT")), // Friulian
    FY_NL(Locale.of("fy", "NL")), // Frisian

    // G-I
    GA_IE(Locale.of("ga", "IE")), // Irish
    GD_GB(Locale.of("gd", "GB")), // Scottish Gaelic
    GL_ES(Locale.of("gl", "ES")), // Galician
    GOT_SE(Locale.of("got", "SE")), // Gothic
    GV_IM(Locale.of("gv", "IM")), // Manx
    HAW_US(Locale.of("haw", "US")), // Hawaiian
    HE_IL(Locale.of("he", "IL")), // Hebrew
    HI_IN(Locale.of("hi", "IN")), // Hindi
    HR_HR(Locale.of("hr", "HR")), // Croatian
    HU_HU(Locale.of("hu", "HU")), // Hungarian
    HY_AM(Locale.of("hy", "AM")), // Armenian
    ID_ID(Locale.of("id", "ID")), // Indonesian
    IG_NG(Locale.of("ig", "NG")), // Igbo
    IO_EN(Locale.of("io", "EN")), // Ido
    IS_IS(Locale.of("is", "IS")), // Icelandic
    ISV(Locale.of("isv", "")), // Interslavic
    IT_IT(Locale.ITALY), // Italian (Italy)

    // J-L
    JA_JP(Locale.JAPAN), // Japanese (Japan)
    JBO_EN(Locale.of("jbo", "EN")), // Lojban
    KA_GE(Locale.of("ka", "GE")), // Georgian
    KK_KZ(Locale.of("kk", "KZ")), // Kazakh
    KN_IN(Locale.of("kn", "IN")), // Kannada
    KO_KR(Locale.KOREA), // Korean (South Korea)
    KSH(Locale.of("ksh", "")), // Kölsch
    KW_GB(Locale.of("kw", "GB")), // Cornish
    LA_LA(Locale.of("la", "LA")), // Latin
    LB_LU(Locale.of("lb", "LU")), // Luxembourgish
    LI_LI(Locale.of("li", "LI")), // Limburgish
    LIJ_IT(Locale.of("lij", "IT")), // Ligurian
    LMO(Locale.of("lmo", "")), // Lombard
    LOL_US(Locale.of("lol", "US")), // LOLCAT
    LT_LT(Locale.of("lt", "LT")), // Lithuanian
    LV_LV(Locale.of("lv", "LV")), // Latvian
    LZH(Locale.of("lzh", "")), // Literary Chinese

    // M-N
    MK_MK(Locale.of("mk", "MK")), // Macedonian
    MN_MN(Locale.of("mn", "MN")), // Mongolian
    MS_MY(Locale.of("ms", "MY")), // Malay
    MT_MT(Locale.of("mt", "MT")), // Maltese
    NAP(Locale.of("nap", "")), // Neapolitan
    NB_NO(Locale.of("nb", "NO")), // Norwegian Bokmål
    NDS_DE(Locale.of("nds", "DE")), // Low German
    NL_BE(Locale.of("nl", "BE")), // Dutch (Belgium)
    NL_NL(Locale.of("nl", "NL")), // Dutch (Netherlands)
    NN_NO(Locale.of("nn", "NO")), // Norwegian Nynorsk
    NU_NG(Locale.of("nu", "NG")), // N'Ko

    // O-R
    OC_FR(Locale.of("oc", "FR")), // Occitan
    OVD(Locale.of("ovd", "")), // Elfdalian
    PL_PL(Locale.of("pl", "PL")), // Polish
    PMS(Locale.of("pms", "")), // Piedmontese
    PT_BR(Locale.of("pt", "BR")), // Portuguese (Brazil)
    PT_PT(Locale.of("pt", "PT")), // Portuguese (Portugal)
    QYA_AA(Locale.of("qya", "AA")), // Quenya
    RO_RO(Locale.of("ro", "RO")), // Romanian
    RPR(Locale.of("rpr", "")), // Russian (Pre-revolutionary)
    RU_RU(Locale.of("ru", "RU")), // Russian

    // S-T
    SE_NO(Locale.of("se", "NO")), // Northern Sami
    SK_SK(Locale.of("sk", "SK")), // Slovak
    SL_SI(Locale.of("sl", "SI")), // Slovenian
    SO_SO(Locale.of("so", "SO")), // Somali
    SQ_AL(Locale.of("sq", "AL")), // Albanian
    SR_SP(Locale.of("sr", "SP")), // Serbian
    SV_SE(Locale.of("sv", "SE")), // Swedish
    SWG(Locale.of("swg", "")), // Swabian
    SZL(Locale.of("szl", "")), // Silesian

    // T-Z
    TA_IN(Locale.of("ta", "IN")), // Tamil
    TH_TH(Locale.of("th", "TH")), // Thai
    TL_PH(Locale.of("tl", "PH")), // Tagalog
    TLH_AA(Locale.of("tlh", "AA")), // Klingon
    TR_TR(Locale.of("tr", "TR")), // Turkish
    TT_RU(Locale.of("tt", "RU")), // Tatar
    UK_UA(Locale.of("uk", "UA")), // Ukrainian
    VAL_ES(Locale.of("val", "ES")), // Valencian
    VEC_IT(Locale.of("vec", "IT")), // Venetian
    VI_VN(Locale.of("vi", "VN")), // Vietnamese
    VP_VL(Locale.of("vp", "VL")), // Viossa
    YI_DE(Locale.of("yi", "DE")), // Yiddish
    YO_NG(Locale.of("yo", "NG")), // Yoruba
    ZH_CN(Locale.SIMPLIFIED_CHINESE), // Chinese (Simplified, China)
    ZH_HK(Locale.of("zh", "HK")), // Chinese (Traditional, Hong Kong)
    ZH_TW(Locale.TRADITIONAL_CHINESE), // Chinese (Traditional, Taiwan)
    ZLM_ARAB(Locale.of("zlm", "ARAB")); // Malay (Jawi)

    private final Locale locale;

    Locales(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Underlying JDK {@link Locale} for this constant.
     *
     * @return locale instance
     */
    public Locale getLocale() {
        return this.locale;
    }
}
