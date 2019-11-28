package coinyser

import java.sql.Timestamp

import coinyser.StreamingProducerSpec._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{Matchers, WordSpec}

class StreamingProducerSpec extends WordSpec with Matchers with TypeCheckedTripleEquals {
  "StreamingProducerApp.deserializeWebsocketTransaction" should {
    "deserialize a valid String to a WebsocketTransaction" in {
      val str =
        """{"amount": 0.045318270000000001, "buy_order_id": 1969499130,
          |"sell_order_id": 1969495276, "amount_str": "0.04531827",
          |"price_str": "6339.73", "timestamp": "1533797395",
          |"price": 6339.7299999999996, "type": 0, "id":
          71826763}""".stripMargin
      StreamingProducerApp.deserializeWebsocketTransaction(str) should ===(SampleWebsocketTransaction)
    }
  }

  "StreamingProducerApp.convertTransaction" should {
    "convert a WebsocketTransaction to Transaction" in {
      StreamingProducerApp.convertWsTransaction
      (SampleWebsocketTransaction) should === (SampleTransaction)
    }
  }

  "StreamingProducer.serializeTransaction" should {
    "serialize a Transaction to a String" in {
      StreamingProducerApp.serializeTransaction
      (SampleTransaction) should ===(SampleJsonTransaction)
    }
  }
}

object StreamingProducerSpec {
  val SampleWebsocketTransaction = WebsocketTransaction(
    amount = 0.04531827, buy_order_id = 1969499130, sell_order_id =
      1969495276, amount_str = "0.04531827", price_str = "6339.73",
    timestamp = "1533797395", price = 6339.73, `type` = 0, id =
      71826763)
  val SampleTransaction = Transaction(
    timestamp = new Timestamp(1533797395000L), tid = 71826763,
    price = 6339.73, sell = false, amount = 0.04531827)

  val SampleJsonTransaction =
    """{"timestamp":"2018-08-09 06:49:55",
      |"date":"2018-08-09","tid":71826763,"price":6339.73,"sell":false,
      |"amount":0.04531827}""".stripMargin
}
