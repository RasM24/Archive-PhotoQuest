package endroad.photoquest.component

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import endroad.photoquest.R

val Class<out Fragment>.key get() = this::class.java.canonicalName
val Fragment.key get() = this::class.java.key

/**
 * Очищает текущий стек фрагментов и начинает новую цепочку
 *
 * @param fragment экземпляр фрагмента
 * @param container id layout'а, в котором будет заменен фрагмент
 */
fun FragmentManager.changeRoot(fragment: Fragment, @IdRes container: Int = R.id.container) {
	for (i in 0 until backStackEntryCount) popBackStack()

	beginTransaction()
			.replace(container, fragment)
			.commitAllowingStateLoss()
}

/**
 * Добавляет в стек новый фрагмент и ставит его текущим
 *
 * @param fragment экземпляр фрагмента
 * @param container id layout'а, в котором будет заменен фрагмент
 */
fun FragmentManager.forwardTo(fragment: Fragment, @IdRes container: Int = R.id.container) {
	findFragmentById(container)?.onHiddenChanged(true)
	beginTransaction()
			.replace(container, fragment)
			.addToBackStack(fragment.key)
			.commitAllowingStateLoss()
}

/**
 * Удаляет из стека последний фрагмент
 */
fun FragmentManager.back() {
	this.popBackStack()
}

/**
 * Закрывает текущий фрагмент.
 */
fun Fragment.finish() {
	requireFragmentManager().back()
}