package com.hotmail.or_dvir.appsfactory_lastfm.other

import com.squareup.moshi.Moshi

object SMyMoshi
{
    internal val instance: Moshi =
        Moshi.Builder()
            //todo keep for reference.
            // remove if not needed
//            .add(CalendarAdapter())
//            .add(
//                PolymorphicJsonAdapterFactory.of(BaseItem::class.java, BaseItem.MOSHI_ITEM_TYPE)
//                    .withSubtype(UserList::class.java, MoshiItemType.USER_LIST.toString())
//                    .withSubtype(ListItem::class.java, MoshiItemType.LIST_ITEM.toString())
//            )
            .build()

    //todo keep for reference.
    // remove if not needed
//    private val adapterMap = instance.adapter(Map::class.java)
//    fun toJson(map: Map<String, Any>): String = adapterMap.toJson(map)
//
//    private val adapterUserList = instance.adapter(UserList::class.java)
//    fun toJson(list: UserList): String = adapterUserList.toJson(list)
//    fun fromJsonUserList(json: String): UserList? = adapterUserList.fromJson(json)
//
//    private val adapterListItem = instance.adapter(ListItem::class.java)
//    fun toJson(item: ListItem): String = adapterListItem.toJson(item)
//    fun fromJsonListItem(json: String): ListItem? = adapterListItem.fromJson(json)
//
//    private val adapterDateTimeItem = instance.adapter(DateTimeItem::class.java)
//    fun toJson(item: DateTimeItem): String = adapterDateTimeItem.toJson(item)
//    fun fromJsonDateTimeItem(json: String): DateTimeItem? = adapterDateTimeItem.fromJson(json)
//
//    private val adapterMainItem = instance.adapter(MainItem::class.java)
//    fun toJson(item: MainItem): String = adapterMainItem.toJson(item)
//    fun fromJsonMainItem(json: String): MainItem? = adapterMainItem.fromJson(json)
//
//    private val adapterPersonalNoteItem = instance.adapter(PersonalNoteItem::class.java)
//    fun toJson(item: PersonalNoteItem): String = adapterPersonalNoteItem.toJson(item)
//    fun fromJsonPersonalNoteItem(json: String): PersonalNoteItem? =
//        adapterPersonalNoteItem.fromJson(json)
//
//    private val adapterRegularItem = instance.adapter(RegularItem::class.java)
//    fun toJson(item: RegularItem): String = adapterRegularItem.toJson(item)
//    fun fromJsonRegularItem(json: String): RegularItem? = adapterRegularItem.fromJson(json)
//
//    private val adapterSpinnerItem = instance.adapter(SpinnerItem::class.java)
//    fun toJson(item: SpinnerItem): String = adapterSpinnerItem.toJson(item)
//    fun fromJsonSpinnerItem(json: String): SpinnerItem? = adapterSpinnerItem.fromJson(json)
//
//    private val typeArrayMyList =
//        Types.newParameterizedType(MutableList::class.java, MyList::class.java)
//    var adapterMyList: JsonAdapter<List<MyList>> = instance.adapter(typeArrayMyList)
//    fun toJson(myLists: List<MyList>): String = adapterMyList.toJson(myLists)
//    fun fromJsonArrayMyLists(json: String): List<MyList>? = adapterMyList.fromJson(json)


    //todo keep for reference.
    //remove if not needed
//    ////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////
//
//    @Suppress("unused", "HasPlatformType")
//    private class UUIDAdapter {
//        @ToJson
//        fun toJson(uuid: UUID) = uuid.toString()
//
//        @FromJson
//        fun fromJson(s: String) = UUID.fromString(s)
//    }
//
}